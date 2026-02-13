package com.martdev.video

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataUseCase
import com.martdev.domain.videodata.VideoPost
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class VideoPagingSourceTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: VideoDataUseCase

    val mockVideos = (1L..40L).map {
        VideoData(id = it)
    }

    val config = PagingConfig(20)

    @Test
    fun `load returns page when on successful load of item keyed data`() = runTest {
        every {
            useCase.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Success(
                VideoPost(
                    mockVideos.take(20),
                    20
                )
            )
        )

        val pagingSource = VideoPagingSource(useCase)

        val pager = TestPager(config, pagingSource)

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertThat(result.data).containsExactlyElementsIn(mockVideos.take(20)).inOrder()

        assertThat(result.prevKey).isNull()

        assertThat(result.nextKey).isEqualTo(20)
    }

    @Test
    fun `test consecutive loads`() = runTest {
        every {
            useCase.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Success(
                VideoPost(
                    mockVideos, 0
                )
            )
        )

        val pagingSource = VideoPagingSource(useCase)

        val pager = TestPager(config, pagingSource)

        val page = with(pager) {
            refresh()
            append()
        } as PagingSource.LoadResult.Page

        assertThat(page.data).containsExactlyElementsIn(mockVideos).inOrder()

        verify(exactly = 2) {
            useCase.getVideoPosts(any(), any())
        }
    }

    @Test
    fun `refresh returns error`() = runTest {
        every {
            useCase.getVideoPosts(any(), any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val pagingSource = VideoPagingSource(useCase)

        val pager = TestPager(config, pagingSource)

        val result = pager.refresh()
        assertTrue(result is PagingSource.LoadResult.Error)
        assertThat(result.throwable.message).isEqualTo("error")

        val page = pager.getLastLoadedPage()
        assertThat(page).isNull()
    }
}