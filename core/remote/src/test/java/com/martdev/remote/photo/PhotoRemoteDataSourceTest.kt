package com.martdev.remote.photo

import com.martdev.common.NetworkResult
import com.martdev.remote.client.CREATE_PHOTOS_COMMENT_PATH
import com.martdev.remote.client.PHOTOS_PATH
import com.martdev.remote.client.PHOTO_COMMENTS_PATH
import com.martdev.remote.photo.model.CreatePhotoCommentRequest
import com.martdev.remote.util.badRequestJsonResponse
import com.martdev.remote.util.badRequestMessage
import com.martdev.remote.util.getMockClient
import io.ktor.http.HttpStatusCode
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class PhotoRemoteDataSourceTest {

    private val emptyPhotosJson = "empty_photos.json"
    private val photosJsonResponse = "photos.json"
    private val createPhotoCommentResponseJson = "createCommentResponse.json"
    private val commentsByPhotoPostID = "commentsByPostID.json"

    private val photosPath = "/v1$PHOTOS_PATH"
    private val createPhotoCommentPath = "/v1$CREATE_PHOTOS_COMMENT_PATH".replace("{postID}", "1")
    private val photoCommentsPath = "/v1$PHOTO_COMMENTS_PATH".replace("{postID}", "1")

    private val createComment = CreatePhotoCommentRequest(
        userID = 1,
        content = "content"
    )

    @Test
    fun getAllPhotoPosts_responseOK_returnPhotos() = runTest {
        val client = getMockClient(json = photosJsonResponse, path = photosPath)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        every { constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0) } answers { callOriginal() }
        val result = PhotoRemoteDataSourceImpl(client).getAllPhotoPosts(20, 0).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isNotEmpty())
            assertEquals(34611213, result.data.data.first().id)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0)
        }
    }

    @Test
    fun getAllPhotoPosts_responseOK_returnEmptyList() = runTest {
        val client = getMockClient(json = emptyPhotosJson, path = photosPath)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        every { constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0) } answers { callOriginal() }
        val result = PhotoRemoteDataSourceImpl(client).getAllPhotoPosts(20, 0).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isEmpty())
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0)
        }
    }

    @Test
    fun getAllPhotoPosts_responseCode400_throwBadRequestException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, json = badRequestJsonResponse, path = photosPath)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        every { constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0) } answers { callOriginal() }
        val r = PhotoRemoteDataSourceImpl(client).getAllPhotoPosts(20, 0).first()
        if (r is NetworkResult.Failure.BadRequest) {
            assertEquals(badRequestMessage, r.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0)
        }
    }

    @Test
    fun getAllPhotoPosts_responseCode401_throwUnauthorizedException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        every { constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0) } answers { callOriginal() }
        val r = PhotoRemoteDataSourceImpl(client).getAllPhotoPosts(20, 0).first()
        if (r is NetworkResult.Failure.Unauthorized) {
            assertEquals("Unauthorized", r.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0)
        }
    }

    @Test
    fun getAllPhotoPosts_responseCode404_throwNotFoundException() = runTest {

        val client = getMockClient(statusCode = HttpStatusCode.NotFound, path = photosPath)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        every { constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0) } answers { callOriginal() }
        val r = PhotoRemoteDataSourceImpl(client).getAllPhotoPosts(20, 0).first()
        if (r is NetworkResult.Failure.NotFound) {
            assertEquals("Not Found", r.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getAllPhotoPosts(20, 0)
        }
    }

    @Test
    fun createCommentsForPhoto_responseOK_returnResponse() = runTest {
        val client = getMockClient(json = createPhotoCommentResponseJson, path = createPhotoCommentPath)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreatePhotoCommentRequest>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.created)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).postComment(any(), any())
        }
    }

    @Test
    fun createCommentsForPhoto_responseBadRequest() = runTest {
        val client = getMockClient(json = badRequestJsonResponse, path = createPhotoCommentPath, statusCode = HttpStatusCode.BadRequest)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreatePhotoCommentRequest>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Failure) {
            assertEquals(badRequestMessage, result.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).postComment(any(), any())
        }
    }

    @Test
    fun createCommentsForPhoto_responseNotFound() = runTest {
        val client = getMockClient(path = createPhotoCommentPath, statusCode = HttpStatusCode.NotFound)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreatePhotoCommentRequest>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Not Found", result.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).postComment(any(), any())
        }
    }

    @Test
    fun createCommentsForPhoto_responseInternalServerError() = runTest {
        val client = getMockClient(path = createPhotoCommentPath, statusCode = HttpStatusCode.InternalServerError)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()
        val commentRequestSlot = slot<CreatePhotoCommentRequest>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .postComment(capture(postIDSlot), capture(commentRequestSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            assertEquals(createComment, commentRequestSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).postComment("1", createComment).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).postComment(any(), any())
        }
    }

    @Test
    fun getCommentsByPhotoPostID_responseOK_returnResponse() = runTest {
        val client = getMockClient(json = commentsByPhotoPostID, path = photoCommentsPath)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).getCommentsByPostID("1").first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.isNotEmpty())
            assertEquals("content 1", result.data.data.first().content)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getCommentsByPostID(any())
        }
    }

    @Test
    fun getCommentsByPhotoPostID_responseNotFound() = runTest {
        val client = getMockClient(path = photoCommentsPath, statusCode = HttpStatusCode.NotFound)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).getCommentsByPostID("1").first()
        if (result is NetworkResult.Failure) {
            assertEquals("Not Found", result.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getCommentsByPostID(any())
        }
    }

    @Test
    fun getCommentsByPhotoPostID_responseInternalServerError() = runTest {
        val client = getMockClient(path = photoCommentsPath, statusCode = HttpStatusCode.InternalServerError)
        mockkConstructor(PhotoRemoteDataSourceImpl::class)

        val postIDSlot = slot<String>()

        every {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client))
                .getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            callOriginal()
        }
        val result = PhotoRemoteDataSourceImpl(client).getCommentsByPostID("1").first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<PhotoRemoteDataSourceImpl>(EqMatcher(client)).getCommentsByPostID(any())
        }
    }
}