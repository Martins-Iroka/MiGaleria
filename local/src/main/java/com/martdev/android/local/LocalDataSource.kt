package com.martdev.android.local

import com.martdev.android.domain.Result


interface LocalDataSource<T, R> {

    suspend fun getData(): Result<List<R>>

    suspend fun saveData(data: T)

    suspend fun deleteData()
}