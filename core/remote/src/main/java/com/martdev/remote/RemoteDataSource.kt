package com.martdev.remote

import kotlinx.coroutines.flow.Flow

interface RemoteDataSource<T> {

    fun load(): Flow<T>
}