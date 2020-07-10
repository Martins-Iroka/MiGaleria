package com.martdev.android.data.usecase

import com.martdev.android.domain.Result

interface UseCase<T> {

    suspend operator fun invoke(query: String, networkConnected: Boolean): Result<List<T>>
}