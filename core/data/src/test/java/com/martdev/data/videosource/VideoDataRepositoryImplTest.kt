@file:Suppress("UnusedFlow")

package com.martdev.data.videosource

import com.martdev.common.NetworkResult
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoDataSource
import com.martdev.local.videodatasource.VideoLocalDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.user.UserData
import com.martdev.remote.datastore.user.UserStorage
import com.martdev.remote.video.VideoRemoteDataSource
import com.martdev.remote.video.model.CreateVideoCommentRequest
import com.martdev.remote.video.model.CreateVideoCommentResponse
import com.martdev.remote.video.model.VideoFilesResponse
import com.martdev.remote.video.model.VideoPost
import com.martdev.remote.video.model.VideoPostCommentResponse
import com.martdev.remote.video.model.VideoPostListResponse
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VideoDataRepositoryImplTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var remote: VideoRemoteDataSource

    @MockK
    private lateinit var local: VideoLocalDataSource

    @MockK
    private lateinit var userStorage: UserStorage

    private lateinit var source: VideoDataSource

    @Before
    fun setup() {
        source = VideoDataRepositoryImpl(local, remote, userStorage)
    }

    @Test
    fun `get remote video posts`() = runTest {
        every {
            remote.getVideoPosts(any(), any())
        } returns flowOf(
            NetworkResult.Success(
                data = ResponseDataPayload(
                    data = VideoPostListResponse(
                        videoItems = listOf(
                            VideoPost(id = 1),
                            VideoPost(
                                id = 2,
                                videoFiles = listOf(
                                    VideoFilesResponse(
                                        videoLink = "videoLink1",
                                        videoSize = 1
                                    )
                                )
                            )
                        ),
                        nextOffset = 20
                    )
                )
            )
        )

        val r = source.getVideoPosts(1, 0).first()

        assertTrue(r is ResponseData.Success)
        assertNotNull(r.data)
        assertTrue(r.data!!.videoItems.isNotEmpty())
        assertTrue(r.data!!.videoItems.last().videoFiles.isNotEmpty())
    }

    @Test
    fun `get remote photos response failed`() = runTest {

        every {
            remote.getVideoPosts(any(), any())
        } returns flowOf(
            NetworkResult.Failure.InternalServerError()
        )

        val r = source.getVideoPosts(1, 1).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)
    }

    @Test
    fun `post comment confirm it was successful`() = runTest {

        val postIdSlot = slot<String>()
        val commentSlot = slot<CreateVideoCommentRequest>()
        val createCommentRequest = CreateVideoCommentRequest(
            userID = 5,
            content = "content"
        )

        every {
            userStorage.getUserData()
        } returns flowOf(UserData(5))

        every {
            remote.postComment(capture(postIdSlot), capture(commentSlot))
        } answers {
            assertEquals("1", postIdSlot.captured)
            assertEquals(createCommentRequest, commentSlot.captured)
            flowOf(NetworkResult.Success(ResponseDataPayload(CreateVideoCommentResponse(true))))
        }

        val r = source.postComment("1", "content").first()

        assertTrue(r is ResponseData.Success)

        verify {
            userStorage.getUserData()
            remote.postComment(any(), any())
        }
    }

    @Test
    fun `post comment confirm IllegalStateException was thrown`() = runTest {
        val errorMessage = "no user id found"
        every {
            userStorage.getUserData()
        } returns flow {
            throw IllegalStateException(errorMessage)
        }

        val r = source.postComment("1", "content").first()

        assertTrue(r is ResponseData.Error)
        assertEquals(errorMessage, r.message)

        verify {
            userStorage.getUserData()
        }
        verify(atLeast = 0, atMost = 0) {
            remote.postComment(any(), any())
        }
    }

    @Test
    fun `post comment but response failed due to internal server error`() = runTest {

        val postIdSlot = slot<String>()
        val commentSlot = slot<CreateVideoCommentRequest>()
        val createCommentRequest = CreateVideoCommentRequest(
            userID = 1,
            content = "content"
        )

        every {
            userStorage.getUserData()
        } returns flowOf(UserData(1))

        every {
            remote.postComment(capture(postIdSlot), capture(commentSlot))
        } answers {
            assertEquals("1", postIdSlot.captured)
            assertEquals(createCommentRequest, commentSlot.captured)
            flowOf(NetworkResult.Failure.InternalServerError())
        }

        val r = source.postComment("1", "content").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)

        verify {
            remote.postComment(any(), any())
        }
    }

    @Test
    fun `get comments by post id confirm response is successful`() = runTest {
        val postIDSlot = slot<String>()

        every {
            remote.getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            flowOf(
                NetworkResult.Success(
                    ResponseDataPayload(
                        listOf(
                            VideoPostCommentResponse(
                                content = "content1",
                                createdAt = "2025-12-01",
                                username = "martdev",
                                1
                            ),
                            VideoPostCommentResponse(
                                content = "content2",
                                createdAt = "2025-12-02",
                                username = "martdev",
                                2
                            )
                        )
                    )
                )
            )
        }

        val r = source.getCommentsByPostID("1").first()

        assertTrue(r is ResponseData.Success)
        assertTrue(r.data.isNullOrEmpty().not())
        assertEquals("content2", r.data!!.last().content)
    }

    @Test
    fun `get comments by post id confirm response failed`() = runTest {
        val postIDSlot = slot<String>()

        every {
            remote.getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            flowOf(
                NetworkResult.Failure.InternalServerError()
            )
        }

        val r = source.getCommentsByPostID("1").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)
    }
}