package com.martdev.android.remote.remotephoto

import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.remote.ApiService
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.ResponseResult

class PhotoRemoteDataSource(private val apiService: ApiService)
    : ResponseResult(),
    RemoteDataSource<PhotoData> {

    override suspend fun search(query: String): Result<PhotoData> {
        return getResult { apiService.searchPhotoAsync(query).await() }
    }

    override suspend fun load(): Result<PhotoData> {
        return getResult { apiService.loadPhotoAsync().await() }
    }

}