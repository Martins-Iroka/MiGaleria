package com.martdev.android.remote.remotevideo

import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.remote.ApiService
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.ResponseResult

class VideoRemoteDataSource(private val api: ApiService)
    : ResponseResult(), RemoteDataSource<VideoData>{

    override suspend fun search(query: String): Result<VideoData> {
        return getResult { api.searchVideoAsync(query).await() }
    }

    override suspend fun load(): Result<VideoData> {
        return getResult { api.loadVideoAsync().await() }
    }
}