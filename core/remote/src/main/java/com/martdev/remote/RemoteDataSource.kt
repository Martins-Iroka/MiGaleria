package com.martdev.remote

import kotlinx.coroutines.flow.Flow

interface RemoteDataSource<T> {

    fun search(query: String): Flow<T>

    fun load(): Flow<T>
}