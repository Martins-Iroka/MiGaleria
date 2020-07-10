package com.martdev.android.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.martdev.android.data.paging.RetryCallback
import com.martdev.android.domain.Result
import kotlinx.coroutines.CoroutineScope

interface Repository<T> {

    suspend fun getData(query: String = "", networkConnected: Boolean): Result<List<T>>
}
