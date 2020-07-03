package com.martdev.android.mygallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.martdev.android.data.SourceResult
import com.martdev.android.domain.Result

abstract class BaseViewModel<T> : ViewModel() {

    protected abstract val _searchKeyword: MutableLiveData<String>

    open var isNetworkAvailable: Boolean = false

    abstract val result: LiveData<SourceResult<T>>

    abstract val data: LiveData<PagedList<T>>

    abstract val networkState: LiveData<Result<List<T>>>

    abstract fun search(query: String? = null)

    open fun retryQuery() {
        result.value?.retryCallback?.invoke()
    }
}