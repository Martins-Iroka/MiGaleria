package com.martdev.android.data

import com.martdev.android.domain.Result

interface Repository<T> {

    suspend fun getData(query: String = "", networkConnected: Boolean): Result<List<T>>
}
