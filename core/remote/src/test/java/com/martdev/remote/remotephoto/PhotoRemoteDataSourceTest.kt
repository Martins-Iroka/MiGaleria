package com.martdev.remote.remotephoto

import com.martdev.remote.BadRequestException
import com.martdev.remote.Client
import com.martdev.remote.NotFoundException
import com.martdev.remote.UnauthorizedException
import com.martdev.remote.util.emptyPhotosJson
import com.martdev.remote.util.photoJsonBody
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
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

@Suppress("UnusedFlow")
class PhotoRemoteDataSourceTest {

    private fun getMockEngine(statusCode: HttpStatusCode = HttpStatusCode.OK, json: String = photoJsonBody) = MockEngine {
        respond(
            content = ByteReadChannel(text = json),
            status = statusCode,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }


    @Test
    fun searchBatman_responseOK_returnList() = runTest {
        val mockEngine = getMockEngine()
        val client = Client(mockEngine)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).search(any()) } answers { callOriginal() }
        val searchedResult = PhotoRemoteDataSource(client).search("batman").first()
        assertTrue(searchedResult.photos.isNotEmpty())
        assertEquals("Ronê Ferreira", searchedResult.photos.first().photographer)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).search("batman")
        }
    }

    @Test
    fun loadAllPhotos_responseOK_returnList() = runTest {
        val mockEngine = getMockEngine()
        val client = Client(mockEngine)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val result = PhotoRemoteDataSource(client).load().first()
        assertTrue(result.photos.isNotEmpty())
        assertEquals("Ronê Ferreira", result.photos.first().photographer)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseOK_returnEmptyList() = runTest {
        val mockEngine = getMockEngine(json = emptyPhotosJson)
        val client = Client(mockEngine)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val result = PhotoRemoteDataSource(client).load().first()
        assertTrue(result.photos.isEmpty())
        assertEquals(0, result.total_result)

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode400_throwBadRequestException() = runTest {

        val mockEngine = getMockEngine(statusCode = HttpStatusCode.BadRequest)
        val client = Client(mockEngine)
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

        val mockEngine = getMockEngine(statusCode = HttpStatusCode.Unauthorized)
        val client = Client(mockEngine)
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

        val mockEngine = getMockEngine(statusCode = HttpStatusCode.NotFound)
        val client = Client(mockEngine)
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
}