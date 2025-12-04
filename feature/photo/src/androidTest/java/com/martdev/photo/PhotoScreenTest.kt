package com.martdev.photo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.martdev.domain.photodata.PhotoData
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class PhotoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun photoScreen_displaysPhotos() {
        val mockPhotos = listOf(
            PhotoData(1, original = "Original 1"),
            PhotoData(2, original = "Original 2")
        )

        val pagingData = PagingData.from(mockPhotos)
        val photoFlow = flowOf(pagingData)

        with(composeTestRule) {
            setContent {
                val lazyPagingItems = photoFlow.collectAsLazyPagingItems()

                PhotoScreen(photos = lazyPagingItems)
            }

            onNodeWithText("Original 1").assertIsDisplayed()
            onNodeWithText("Original 2").assertIsDisplayed()
        }
    }
}