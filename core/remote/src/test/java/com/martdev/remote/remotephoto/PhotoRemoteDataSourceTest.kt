package com.martdev.remote.remotephoto

import com.martdev.remote.BadRequestException
import com.martdev.remote.Client
import com.martdev.remote.NotFoundException
import com.martdev.remote.UnauthorizedException
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.util.FakeTokenStorage
import com.martdev.remote.util.readJsonFile
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class PhotoRemoteDataSourceTest {

    private lateinit var client: TokenStorage
    private val emptyPhotosJson = "empty_photos.json"
    @Before
    fun setup() {
        client = FakeTokenStorage()
    }

    @Test
    fun loadAllPhotos_responseOK_returnList() = runTest {
        val client = getMockClient()
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val result = PhotoRemoteDataSource(client).load().first()
        assertTrue(result.data.isNotEmpty())
        assertEquals(34611213, result.data.first().id)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseOK_returnEmptyList() = runTest {
        val client = getMockClient(json = emptyPhotosJson)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val result = PhotoRemoteDataSource(client).load().first()
        assertTrue(result.data.isEmpty())

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode400_throwBadRequestException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.BadRequest)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val b = assertFailsWith<BadRequestException> {
            PhotoRemoteDataSource(client).load().first()
        }

        assertEquals("Bad Request", b.error)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode401_throwUnauthorizedException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val u = assertFailsWith<UnauthorizedException> {
            PhotoRemoteDataSource(client).load().first()
        }

        assertEquals("Unauthorized", u.error)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode404_throwNotFoundException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.NotFound)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val n = assertFailsWith<NotFoundException> {
            PhotoRemoteDataSource(client).load().first()
        }

        assertEquals("Not Found", n.error)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    private fun getMockClient(statusCode: HttpStatusCode = HttpStatusCode.OK, json: String = "photos.json") = Client(
        MockEngine {
            respond(
                content = ByteReadChannel(content = readJsonFile(json)),
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        },
        client
    )
}