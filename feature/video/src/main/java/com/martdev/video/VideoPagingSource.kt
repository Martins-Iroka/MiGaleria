package com.martdev.video

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataUseCase
import kotlinx.coroutines.flow.first

class VideoPagingSource(
    private val useCase: VideoDataUseCase
) : PagingSource<Int, VideoData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoData> {
        val currentPage = params.key ?: 0
        return try {
            when(val r = useCase.getVideoPosts(20, currentPage).first()) {
                is ResponseData.Success -> {
                    val videos = r.data?.videoItems.orEmpty()
                    val next = r.data?.nextOffset?.takeIf { it >= 0 }
                    LoadResult.Page(
                        data = videos,
                        prevKey = null,
                        nextKey = next
                    )
                }
                is ResponseData.Error -> LoadResult.Error(Exception(r.message))

                else -> LoadResult.Error(IllegalStateException("unexpected response type"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideoData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1)
        }
    }

}