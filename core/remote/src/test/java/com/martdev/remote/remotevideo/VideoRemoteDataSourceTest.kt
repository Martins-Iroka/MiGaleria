package com.martdev.remote.remotevideo

import com.martdev.remote.NetworkResult
import com.martdev.remote.util.getMockClient
import io.ktor.http.HttpStatusCode
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class VideoRemoteDataSourceTest {

    private val emptyVideoJson = "empty_videos.json"
    private val videoJson = "videos.json"

    @Test
    fun loadAllVideos_responseOk_returnList() = runTest {
        val client = getMockClient(json = videoJson)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val result = VideoRemoteDataSource(client).load().first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isNotEmpty())
            assertEquals(7677511, result.data.data.first().id)
        }

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllVideos_responseOk_returnEmptyList() = runTest {
        val client = getMockClient(json = emptyVideoJson)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }

        val result = VideoRemoteDataSource(client).load().first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isEmpty())
        }
    }

    @Test
    fun loadAllVideos_responseCode400_throwBadRequestException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.BadRequest)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val r = VideoRemoteDataSource(client).load().first()
        if (r is NetworkResult.Failure) {
            Assert.assertEquals("Bad Request", r.error)
        }

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllVideos_responseCode401_throwUnauthorizedException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val r = VideoRemoteDataSource(client).load().first()
        if (r is NetworkResult.Failure) {
            Assert.assertEquals("Unauthorized", r.error)
        }

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllVideos_responseCode404_throwNotFoundException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.NotFound)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val r = VideoRemoteDataSource(client).load().first()
        if (r is NetworkResult.Failure) {
            Assert.assertEquals("Not Found", r.error)
        }

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }
}