package com.martdev.android.remote

import com.martdev.android.domain.Result

interface RemoteDataSource<T> {

    suspend fun search(query: String): Result<T>

    suspend fun load(): Result<T>
}