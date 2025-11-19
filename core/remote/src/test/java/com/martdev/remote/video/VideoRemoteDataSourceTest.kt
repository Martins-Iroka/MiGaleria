package com.martdev.remote.video

import com.martdev.common.NetworkResult
import com.martdev.remote.CREATE_VIDEOS_COMMENT_PATH
import com.martdev.remote.VIDEOS_PATH
import com.martdev.remote.VIDEO_COMMENTS_PATH
import com.martdev.remote.util.badRequestJsonResponse
import com.martdev.remote.util.badRequestMessage
import com.martdev.remote.util.getMockClient
import com.martdev.remote.video.model.CreateVideoCommentRequest
import io.ktor.http.HttpStatusCode
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.slot
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
    private val createVideoCommentResponseJson = "createCommentResponse.json"
    private val commentsByVideoPostID = "commentsByPostID.json"

    private val videoPath = "/v1$VIDEOS_PATH"
    private val createVideoCommentPath = "/v1$CREATE_VIDEOS_COMMENT_PATH".replace("{postID}", "1")
    private val videoCommentPath = "/v1$VIDEO_COMMENTS_PATH".replace("{postID}", "1")

    private val createComment = CreateVideoCommentRequest(
        userID = 1,
        content = "content"
    )

    @Test
    fun getAllVideoPosts_responseOk_returnList() = runTest {
        val client = getMockClient(json = videoJson, path = videoPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        every { constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
            .getAllVideoPosts(20, 0) } answers { callOriginal() }
        val result = VideoRemoteDataSourceImpl(client).getAllVideoPosts(20, 0).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isNotEmpty())
            assertEquals(7677511, result.data.data.first().id)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .getAllVideoPosts(20, 0)
        }
    }

    @Test
    fun getAllVideoPosts_responseOk_returnEmptyList() = runTest {
        val client = getMockClient(json = emptyVideoJson, path = videoPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        every { constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
            .getAllVideoPosts(20, 0) } answers { callOriginal() }

        val result = VideoRemoteDataSourceImpl(client).getAllVideoPosts(20, 0).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isEmpty())
        }
    }

    @Test
    fun getAllVideoPosts_responseCode400_throwBadRequestException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = videoPath, json = badRequestJsonResponse)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        every { constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client)).getAllVideoPosts(20, 0) } answers { callOriginal() }
        val r = VideoRemoteDataSourceImpl(client).getAllVideoPosts(20, 0).first()
        if (r is NetworkResult.Failure) {
            Assert.assertEquals(badRequestMessage, r.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client)).getAllVideoPosts(20, 0)
        }
    }

    @Test
    fun getAllVideoPosts_responseCode401_throwUnauthorizedException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        every { constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client)).getAllVideoPosts(20, 0) } answers { callOriginal() }
        val r = VideoRemoteDataSourceImpl(client).getAllVideoPosts(20, 0).first()
        if (r is NetworkResult.Failure) {
            Assert.assertEquals("Unauthorized", r.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client)).getAllVideoPosts(20, 0)
        }
    }

    @Test
    fun getAllVideoPosts_responseCode404_throwNotFoundException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.NotFound, path = videoPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        every { constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client)).getAllVideoPosts(20, 0) } answers { callOriginal() }
        val r = VideoRemoteDataSourceImpl(client).getAllVideoPosts(20, 0).first()
        if (r is NetworkResult.Failure) {
            Assert.assertEquals("Not Found", r.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client)).getAllVideoPosts(20, 0)
        }
    }

    @Test
    fun createCommentsForVideo_responseOK_returnResponse() = runTest {
        val client = getMockClient(json = createVideoCommentResponseJson, path = createVideoCommentPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreateVideoCommentRequest>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.created)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).postComment(any(), any())
        }
    }

    @Test
    fun createCommentsForPhoto_responseBadRequest() = runTest {
        val client = getMockClient(json = badRequestJsonResponse,
            path = createVideoCommentPath, statusCode = HttpStatusCode.BadRequest)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreateVideoCommentRequest>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Failure) {
            assertEquals(badRequestMessage, result.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(any(), any())
        }
    }

    @Test
    fun createCommentsForPhoto_responseNotFound() = runTest {
        val client = getMockClient(
            path = createVideoCommentPath, statusCode = HttpStatusCode.NotFound)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreateVideoCommentRequest>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Not Found", result.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(any(), any())
        }
    }

    @Test
    fun createCommentsForPhoto_responseInternalServerError() = runTest {
        val client = getMockClient(
            path = createVideoCommentPath, statusCode = HttpStatusCode.InternalServerError)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreateVideoCommentRequest>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(any(), any())
        }
    }

    @Test
    fun getCommentsByVideoPostID_responseOK_returnResponse() = runTest {
        val client = getMockClient(json = commentsByVideoPostID, path = videoCommentPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).getCommentsByPostID("1").first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isNotEmpty())
            assertEquals("content 1", result.data.data.first().content)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).getCommentsByPostID(any())
        }
    }

    @Test
    fun getCommentsByVideoPostID_responseNotFound() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.NotFound, path = videoCommentPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).getCommentsByPostID("1").first()
        if (result is NetworkResult.Failure) {
            assertEquals("Not Found", result.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).getCommentsByPostID(any())
        }
    }

    @Test
    fun getCommentsByVideoPostID_responseInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = videoCommentPath)
        mockkConstructor(VideoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()

        every {
            constructedWith<VideoRemoteDataSourceImpl>(EqMatcher(client))
                .getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            callOriginal()
        }

        val result =
            VideoRemoteDataSourceImpl(client).getCommentsByPostID("1").first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<VideoRemoteDataSourceImpl>(
                EqMatcher(client)
            ).getCommentsByPostID(any())
        }
    }
}