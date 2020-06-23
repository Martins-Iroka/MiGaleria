package com.martdev.android.remote.remotephoto

import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.remote.ApiService
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.ResponseResult

class PhotoRemoteDataSource(private val apiService: ApiService)
    : ResponseResult(),
    RemoteDataSource<PhotoData> {

    override suspend fun search(
        query: String?,
        per_page: Int?,
        page: Int?
    ): Result<List<PhotoData>> {
        return getResult { apiService.searchPhotoAsync(query, per_page, page).await() }
    }

    override suspend fun load(per_page: Int, page: Int): Result<List<PhotoData>> {
        return getResult { apiService.loadPhotoAsync(per_page, page).await() }
    }

}