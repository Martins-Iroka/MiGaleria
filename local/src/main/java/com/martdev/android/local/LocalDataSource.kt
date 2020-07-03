package com.martdev.android.local

import androidx.paging.DataSource

interface LocalDataSource<T, R> {

    fun getData(): DataSource.Factory<Int, R>

    suspend fun saveData(data: T)

    suspend fun deleteData()
}