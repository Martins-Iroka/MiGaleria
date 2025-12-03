package com.martdev.photo

import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PhotoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

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
    fun `test pager photo list`() = runTest {
        val mockPhotos = (1L..40).map {
            PhotoData(photoId = it)
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

        val items = viewmodel.photoList.asSnapshot()

        assertThat(items).containsExactlyElementsIn(mockPhotos).inOrder()
    }
}