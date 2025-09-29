package com.martdev.remote.remotevideo

import com.martdev.remote.BadRequestException
import com.martdev.remote.Client
import com.martdev.remote.NotFoundException
import com.martdev.remote.UnauthorizedException
import com.martdev.remote.util.emptyVideoJson
import com.martdev.remote.util.videoJsonBody
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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class VideoRemoteDataSourceTest {

    private fun getMockClient(statusCode: HttpStatusCode = HttpStatusCode.OK, json: String = videoJsonBody) =
        Client(
            MockEngine {
                respond(
                    content = ByteReadChannel(text = json),
                    status = statusCode,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )

    @Test
    fun searchBatmanVideo_responseOK_returnList() = runTest {
        val client = getMockClient()
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).search(any()) } answers { callOriginal() }
        val searchResult = VideoRemoteDataSource(client).search("batman").first()
        assertTrue(searchResult.videos.isNotEmpty())
        assertEquals(
            2, searchResult.per_page
        )
        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).search("batman")
        }
    }

    @Test
    fun loadAllVideos_responseOk_returnList() = runTest {
        val client = getMockClient()
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val result = VideoRemoteDataSource(client).load().first()
        assertTrue(result.videos.isNotEmpty())
        assertEquals(3716105, result.videos.first().user.id)

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
        assertTrue(result.videos.isEmpty())
        Assert.assertEquals(0, result.total_results)
    }

    @Test
    fun loadAllVideos_responseCode400_throwBadRequestException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.BadRequest)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val b = assertFailsWith<BadRequestException> {
            VideoRemoteDataSource(client).load().first()
        }

        Assert.assertEquals("Bad Request", b.error)

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllVideos_responseCode401_throwUnauthorizedException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val u = assertFailsWith<UnauthorizedException> {
            VideoRemoteDataSource(client).load().first()
        }

        Assert.assertEquals("Unauthorized", u.error)

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }

    @Test
    fun loadAllVideos_responseCode404_throwNotFoundException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.NotFound)
        mockkConstructor(VideoRemoteDataSource::class)

        every { constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load() } answers { callOriginal() }
        val n = assertFailsWith<NotFoundException> {
            VideoRemoteDataSource(client).load().first()
        }

        Assert.assertEquals("Not Found", n.error)

        verify {
            constructedWith<VideoRemoteDataSource>(EqMatcher(client)).load()
        }
    }
}