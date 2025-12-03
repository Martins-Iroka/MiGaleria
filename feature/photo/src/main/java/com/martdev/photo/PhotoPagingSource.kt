package com.martdev.photo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataUseCase
import kotlinx.coroutines.flow.first

class PhotoPagingSource(
    val useCase: PhotoDataUseCase
) : PagingSource<Int, PhotoData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoData> {
        val currentPage = params.key ?: 1
        val limit = params.loadSize
        return try {
            when (val r = useCase.getPhotos(limit, currentPage).first()) {
                is ResponseData.Success -> {

                    val photos= r.data.orEmpty()
                    LoadResult.Page(
                        data = photos,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (photos.isNotEmpty()) currentPage + 1 else null
                    )
                }

                is ResponseData.Error -> {
                    LoadResult.Error(Exception(r.message))
                }

                else -> LoadResult.Error(IllegalStateException("Unexpected response type"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}