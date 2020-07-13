package com.martdev.android.domain

interface Repository<T> {

    suspend fun getData(query: String = "", networkConnected: Boolean): Result<List<T>>
}
