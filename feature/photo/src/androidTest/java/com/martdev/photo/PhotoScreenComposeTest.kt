package com.martdev.photo

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.compose.collectAsLazyPagingItems
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoInfo
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PhotoScreenComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var photoDataSource: PhotoDataSource

    private lateinit var viewmodel: PhotoViewModel

    @Before
    fun setUp() {
        val useCase = PhotoDataUseCase(photoDataSource)
        viewmodel = PhotoViewModel(useCase)
    }

    @Test
    fun testPhotoCompose() = runTest {
        val mockPhotos = (1L..40).map {
            PhotoData(photoId = it, original = "https://images.pexels.com/photos/34622874/pexels-photo-34622874.jpeg", photographer = "photographer $it")
        }

        every {
            photoDataSource.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Success(
                PhotoInfo(
                    mockPhotos,
                    40
                ))
        )

        every {
            photoDataSource.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Success(
                PhotoInfo(
                    mockPhotos,
                    -1
                ))
        )

        val items = viewmodel.photoList

        with(composeTestRule) {
            setContent {
                val lazyPagingItems = items.collectAsLazyPagingItems()

                PhotoScreen(photos = lazyPagingItems)
            }

            onNodeWithText("Photo by photographer 1").assertIsDisplayed()

            onNodeWithTag(PHOTO_LAZY_COLUMN).assertIsDisplayed().performScrollToNode(hasText("Photo by photographer 20"))

            onNodeWithText("Photo by photographer 20").assertIsDisplayed()

            onNodeWithTag(PHOTO_LAZY_COLUMN).assertIsDisplayed().performScrollToNode(hasText("Photo by photographer 40"))

            onNodeWithText("Photo by photographer 40").assertIsDisplayed()
        }
    }
}