package com.martdev.android.data.usecase

import com.martdev.android.data.SourceResult
import kotlinx.coroutines.CoroutineScope

interface UseCase<T> {

    operator fun invoke(query: String?, scope: CoroutineScope, networkConnected: Boolean): SourceResult<T>
}