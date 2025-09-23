package com.martdev.domain

import kotlinx.coroutines.flow.Flow

interface Repository<T> {

    fun getData(query: String = ""): Flow<List<T>>
}
