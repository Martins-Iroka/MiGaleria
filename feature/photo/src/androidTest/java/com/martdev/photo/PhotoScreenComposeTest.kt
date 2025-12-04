package com.martdev.photo

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.paging.compose.collectAsLazyPagingItems
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataUseCase
import io.mockk.coEvery
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
    private lateinit var useCase: PhotoDataUseCase

    private lateinit var viewmodel: PhotoViewModel

    @Before
    fun setUp() {
        viewmodel = PhotoViewModel(useCase)
    }

    @Test
    fun testPhotoCompose() = runTest {
        val mockPhotos = (1L..40).map {
            PhotoData(photoId = it, original = "Original $it")
        }

        coEvery {
            useCase.getPhotos(any(), 1)
        } returns flowOf(
            ResponseData.Success(mockPhotos.take(20))
        )

        coEvery {
            useCase.getPhotos(any(), 2)
        } returns flowOf(
            ResponseData.Success(mockPhotos.drop(20))
        )

        val items = viewmodel.photoList

        with(composeTestRule) {
            setContent {
                val lazyPagingItems = items.collectAsLazyPagingItems()

                PhotoScreen(photos = lazyPagingItems)
            }

            onNodeWithText("Original 1").assertIsDisplayed()
            onNodeWithText("Original 20").assertIsDisplayed()
            onNodeWithText("Original 40").assertExists().performScrollTo()
        }
    }
}