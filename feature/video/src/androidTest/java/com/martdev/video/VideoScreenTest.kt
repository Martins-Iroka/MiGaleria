package com.martdev.video

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.common.truth.Truth.assertThat
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataUseCase
import com.martdev.domain.videodata.VideoPost
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VideoScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: VideoDataUseCase

    private lateinit var viewModel: VideoViewModel

    @Before
    fun setup() {
        viewModel = VideoViewModel(useCase)
    }

    @Test
    fun testVideoScreen_displayVideos() {
        val mockVideos = (1L..20).map {
            VideoData(id = it, videoImage = "https://images.pexels.com/videos/6963395/eco-friendly-environment-environmentally-friendly-mothernature-6963395.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=630")
        }

        every {
            useCase.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Success(
                data = VideoPost(mockVideos, 20)
            )
        )

        every {
            useCase.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Success(
                VideoPost(
                    mockVideos, -1
                )
            )
        )

        val items = viewModel.videoList

        with(composeTestRule) {
            setContent {
                val lazyPagingItems = items.collectAsLazyPagingItems()

                VideoScreen(lazyPagingItems) {
                    assertThat(it).isEqualTo(15)
                }
            }

            onNodeWithTag(VIDEO_LAZY_COLUMN).assertExists().assertIsDisplayed()

            onNodeWithTag(mockVideos.first().id.toString()).assertIsDisplayed()

            onNodeWithTag(VIDEO_LAZY_COLUMN).performScrollToNode(hasTestTag("8"))

            onNodeWithTag("8").assertIsDisplayed()

            onNodeWithTag(VIDEO_LAZY_COLUMN).performScrollToNode(hasTestTag("15"))

            onNodeWithTag("15").assertIsDisplayed().performClick()
        }
    }
}