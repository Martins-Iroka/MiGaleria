package com.martdev.remote.remotephoto

import com.martdev.remote.NetworkResult
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.util.FakeTokenStorage
import com.martdev.remote.util.getMockClient
import io.ktor.http.HttpStatusCode
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isNotEmpty())
            assertEquals(34611213, result.data.data.first().id)
        }

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
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isEmpty())
        }

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode400_throwBadRequestException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.BadRequest)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val r = PhotoRemoteDataSource(client).load().first()
        if (r is NetworkResult.Failure.BadRequest) {
            assertEquals("Bad Request", r.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode401_throwUnauthorizedException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val r = PhotoRemoteDataSource(client).load().first()
        if (r is NetworkResult.Failure.Unauthorized) {
            assertEquals("Unauthorized", r.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllPhotos_responseCode404_throwNotFoundException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.NotFound)
        mockkConstructor(PhotoRemoteDataSource::class)

        every { constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val r = PhotoRemoteDataSource(client).load().first()
        if (r is NetworkResult.Failure.NotFound) {
            assertEquals("Not Found", r.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSource>(EqMatcher(client)).load()
        }
    }
}