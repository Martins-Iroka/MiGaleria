package com.martdev.domain.usecase

import kotlinx.coroutines.flow.Flow


interface UseCase<T> {

    operator fun invoke(query: String): Flow<List<T>>
}