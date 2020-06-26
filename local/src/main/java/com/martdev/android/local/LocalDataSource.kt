package com.martdev.android.local

import androidx.paging.DataSource

interface LocalDataSource<T> {

    fun getData(): DataSource.Factory<Int, T>

    suspend fun saveData(data: T)

    suspend fun deleteData()
}