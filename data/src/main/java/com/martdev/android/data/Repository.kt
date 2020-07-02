package com.martdev.android.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.martdev.android.data.paging.RetryCallback
import com.martdev.android.domain.Result
import kotlinx.coroutines.CoroutineScope

interface Repository<T> {

    fun getData(query: String?, scope: CoroutineScope, networkConnected: Boolean): SourceResult<T>
}

data class SourceResult<T>(
    val data: LiveData<PagedList<T>>,
    val networkState: LiveData<Result<List<T>>>? = null,
    val retryCallback: RetryCallback? = null
)
