package com.martdev.video

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataUseCase
import com.martdev.domain.videodata.VideoPost
import com.martdev.domain.videodata.VideoPostComments
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class VideoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: VideoDataUseCase

    private lateinit var viewmodel: VideoViewModel

    @Before
    fun setUp() {
        viewmodel = VideoViewModel(useCase)
    }


    @Suppress("UnusedFlow")
    @Test
    fun `test video pager list`() = runTest {

        val mockVideos = (1L..40L).map {
            VideoData(
                id = it
            )
        }

        every {
            useCase.getVideoPosts(20, 0)
        } returns flowOf(
            ResponseData.Success(
                VideoPost(
                    mockVideos.take(20),
                    20
                )
            )
        )

        every {
            useCase.getVideoPosts(20, 20)
        } returns flowOf(
            ResponseData.Success(
                VideoPost(
                    mockVideos.drop(20),
                    -1
                )
            )
        )

        val items = viewmodel.videoList.asSnapshot()

        assertThat(items).containsExactlyElementsIn(mockVideos).inOrder()
        verify(exactly = 2) {
            useCase.getVideoPosts(any(), any())
        }
    }

    @Test
    fun `post comments should emit no response then loading then collect successful response data`() = runTest {
        val postId = slot<String>()
        val content = slot<String>()

        every {
            useCase.postComment(capture(postId), capture(content))
        } answers {
            assertThat(postId.captured).isEqualTo("1")
            assertThat(content.captured).isEqualTo("content")
            flowOf(ResponseData.Success(null))
        }

        every {
            useCase.getCommentsByPostId(capture(postId))
        } answers {
            assertThat(postId.captured).isEqualTo("1")
            flowOf(
                ResponseData.Success(
                    listOf(
                        VideoPostComments(
                            id = 1,
                            content = "content"
                        )
                    )
                )
            )
        }

        viewmodel.createCommentResponse.test {
            assertThat(awaitItem()).isEqualTo(ResponseData.NoResponse)

            viewmodel.postComment("1", "content")

            assertThat(awaitItem()).isEqualTo(ResponseData.Loading)

            assertIs<ResponseData.Success<*>>(awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun `post comments should emit no response then loading then collect error`() = runTest {

        every {
            useCase.postComment(any(), any())
        } returns flowOf(ResponseData.Error("error"))


        viewmodel.createCommentResponse.test {
            assertThat(awaitItem()).isEqualTo(ResponseData.NoResponse)

            viewmodel.postComment("1", "content")

            assertThat(awaitItem()).isEqualTo(ResponseData.Loading)

            val finalResult = awaitItem()
            assertIs<ResponseData.Error>(finalResult)

            assertThat(finalResult.message).isEqualTo("error")

            expectNoEvents()
        }
    }

    @Test
    fun `post comments should emit no response then loading then catch error thrown in flow`() = runTest {

        every {
            useCase.postComment(any(), any())
        } returns flow {
            throw Exception("error")
        }


        viewmodel.createCommentResponse.test {
            assertThat(awaitItem()).isEqualTo(ResponseData.NoResponse)

            viewmodel.postComment("1", "content")

            assertThat(awaitItem()).isEqualTo(ResponseData.Loading)

            val finalResult = awaitItem()
            assertIs<ResponseData.Error>(finalResult)

            assertThat(finalResult.message).isEqualTo("error")

            expectNoEvents()
        }
    }

    @Test
    fun `get comments by post id emit no response then loading then collect successful response data`() = runTest {

        every {
            useCase.getCommentsByPostId(any())
        } returns flowOf(
            ResponseData.Success(
                listOf(
                    VideoPostComments(
                        id = 1,
                        content = "content"
                    )
                )
            )
        )

        viewmodel.videoComments.test {
            assertThat(awaitItem()).isEqualTo(ResponseData.NoResponse)

            viewmodel.getCommentsByPostId("1")

            assertThat(awaitItem()).isEqualTo(ResponseData.Loading)

            val finalResult = awaitItem()

            assertTrue(finalResult is ResponseData.Success)
            assertThat(finalResult.data).isNotEmpty()
        }
    }

    @Test
    fun `get comments by post id emit no response then loading then collect error data`() = runTest {

        every {
            useCase.getCommentsByPostId(any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        viewmodel.videoComments.test {
            assertThat(awaitItem()).isEqualTo(ResponseData.NoResponse)

            viewmodel.getCommentsByPostId("1")

            assertThat(awaitItem()).isEqualTo(ResponseData.Loading)

            val finalResult = awaitItem()

            assertTrue(finalResult is ResponseData.Error)
            assertThat(finalResult.message).isEqualTo("error")
        }
    }

    @Test
    fun `get comments by post id emit no response then loading then catch error thrown in flow`() = runTest {

        every {
            useCase.getCommentsByPostId(any())
        } returns flow{
            throw Exception("error from flow")
        }

        viewmodel.videoComments.test {
            assertThat(awaitItem()).isEqualTo(ResponseData.NoResponse)

            viewmodel.getCommentsByPostId("1")

            assertThat(awaitItem()).isEqualTo(ResponseData.Loading)

            val finalResult = awaitItem()

            assertTrue(finalResult is ResponseData.Error)
            assertThat(finalResult.message).isNotEqualTo("error")
            assertThat(finalResult.message).isEqualTo("error from flow")
        }
    }
}