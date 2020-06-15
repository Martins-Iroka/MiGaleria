package com.martdev.android.domain

interface RemoteDataSource<T> {

    suspend fun search(query: String, per_page: Int, page: Int): Result<List<T>>

    suspend fun load(per_page: Int, page: Int): Result<List<T>>
}