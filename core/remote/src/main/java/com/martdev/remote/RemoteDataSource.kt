package com.martdev.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RemoteDataSource<T> {

    fun search(query: String): Flow<T> = flowOf()

    fun load(): Flow<T>
}