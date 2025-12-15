package com.martdev.domain.usecase

import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataSource
import com.martdev.domain.videodata.VideoDataUseCase
import com.martdev.domain.videodata.VideoFileData
import com.martdev.domain.videodata.VideoPost
import com.martdev.domain.videodata.VideoPostComments
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class VideoDataUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var videoDataSource: VideoDataSource

    private lateinit var videoUC: VideoDataUseCase

    @Before
    fun setUp() {
        clearMocks(videoDataSource)
        videoUC = VideoDataUseCase(videoDataSource)
    }

    @Test
    fun `get video posts response is successful`() = runTest {
        every {
            videoDataSource.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Success(
                VideoPost(
                    videoItems = listOf(
                        VideoData(
                            id = 1
                        ),
                        VideoData(
                            id = 2,
                            videoFiles = listOf(
                                VideoFileData(
                                    link = "link1",
                                    size = 1
                                ),
                                VideoFileData(
                                    link = "link2",
                                    size = 2
                                )
                            )
                        )
                    ),
                    nextOffset = 20
                )
            )
        )

        val r = videoUC.getVideoPosts(1, 0).first()

        assertTrue(r is ResponseData.Success)
        assertNotNull(r.data)
        assertTrue(r.data.videoItems.isNotEmpty())
        assertTrue(r.data.videoItems.last().videoFiles.isNotEmpty())

        verify {
            videoDataSource.getVideoPosts(any(), any())
        }
    }

    @Test
    fun `get video posts response failed`() = runTest {
        every {
            videoDataSource.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val r = videoUC.getVideoPosts(1, 0).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("error", r.message)

        verify {
            videoDataSource.getVideoPosts(any(), any())
        }
    }

    @Test
    fun `post comments response is successful`() = runTest {

        every {
            videoDataSource.postComment(any(), any())
        } returns flowOf(
            ResponseData.Success(null)
        )

        val r = videoUC.postComment("1", "content").first()

        assertTrue(r is ResponseData.Success)

        verify {
            videoDataSource.postComment(any(), any())
        }
    }

    @Test
    fun `post comments response failed`() = runTest {

        every {
            videoDataSource.postComment(any(), any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val r = videoUC.postComment("1", "content").first()

        assertTrue(
            r is ResponseData.Error
        )
        assertEquals("error", r.message)

        verify {
            videoDataSource.postComment(any(),any())
        }
    }

    @Test
    fun `get comments by post id response is successful`() = runTest {
        every {
            videoDataSource.getCommentsByPostID(any())
        } returns flowOf(
            ResponseData.Success(
                listOf(
                    VideoPostComments(
                        content = "content",
                        createdAt = "10/10/10",
                        username = "username",
                        id = 1
                    ),
                    VideoPostComments(
                        content = "content",
                        createdAt = "10/10/10",
                        username = "username",
                        id = 2
                    ),
                    VideoPostComments(
                        content = "content",
                        createdAt = "10/10/10",
                        username = "username",
                        id = 3
                    )
                )
            )
        )

        val r = videoUC.getCommentsByPostId("1").first()

        assertTrue(r is ResponseData.Success)
        assertNotNull(r.data)
        assertTrue(r.data.isNotEmpty())
    }

    @Test
    fun `get comments by post id response failed`() = runTest {
        every {
            videoDataSource.getCommentsByPostID(any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val r = videoDataSource.getCommentsByPostID("1").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("error", r.message)
    }
}