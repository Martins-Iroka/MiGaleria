package com.martdev.photo

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoInfo
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhotoPagingSourceTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: PhotoDataUseCase

    val mockPhotos = (1L..40).map {
        PhotoData(photoId = it)
    }

    val config = PagingConfig(20)
    @Test
    fun `load returns page when on successful load of item keyed data`() = runTest {
        every {
            useCase.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Success(
                PhotoInfo(
                    mockPhotos.take(20),
                    20
                )
            )
        )

        val pagingSource = PhotoPagingSource(useCase)

        val pager = TestPager(config, pagingSource)

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertThat(result.data).containsExactlyElementsIn(mockPhotos.take(20)).inOrder()

        assertThat(result.prevKey).isNull()

        assertThat(result.nextKey).isEqualTo(20)
    }

    @Test
    fun `test consecutive loads`() = runTest {
        every {
            useCase.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Success(
                PhotoInfo(
                    mockPhotos,
                    0
                )
            )
        )

        val pagingSource = PhotoPagingSource(useCase)

        val pager = TestPager(config, pagingSource)

        val page = with(pager) {
            refresh()
            append()
        } as PagingSource.LoadResult.Page

        assertThat(page.data).containsExactlyElementsIn(mockPhotos).inOrder()
    }

    @Test
    fun `refresh returns error`() = runTest {
        every {
            useCase.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val pagingSource = PhotoPagingSource(useCase)

        val pager = TestPager(config, pagingSource)

        val result = pager.refresh()
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals("error", result.throwable.message)

        val page = pager.getLastLoadedPage()
        assertThat(page).isNull()
    }
}