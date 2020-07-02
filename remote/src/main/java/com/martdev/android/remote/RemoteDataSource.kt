package com.martdev.android.remote

import com.martdev.android.domain.Result

interface RemoteDataSource<T> {

    suspend fun search(query: String? = null, per_page: Int? = null, page: Int? = null): Result<T>

    suspend fun load(per_page: Int, page: Int): Result<T>
}