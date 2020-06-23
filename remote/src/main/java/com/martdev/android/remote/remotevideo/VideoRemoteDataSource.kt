package com.martdev.android.remote.remotevideo

import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.remote.ApiService
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.ResponseResult

class VideoRemoteDataSource(private val api: ApiService)
    : ResponseResult(), RemoteDataSource<VideoData>{

    override suspend fun search(
        query: String?,
        per_page: Int?,
        page: Int?
    ): Result<List<VideoData>> {
        return getResult { api.searchVideoAsync(query, per_page, page).await() }
    }

    override suspend fun load(per_page: Int, page: Int): Result<List<VideoData>> {
        return getResult { api.loadVideoAsync(per_page, page).await() }
    }
}