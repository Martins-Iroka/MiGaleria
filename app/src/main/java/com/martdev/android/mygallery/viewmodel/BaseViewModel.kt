package com.martdev.android.mygallery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.domain.Result
import com.martdev.android.mygallery.utils.Event

abstract class BaseViewModel<T> : ViewModel() {

    abstract val searchKeyword: MutableLiveData<String>

    abstract val data: MutableLiveData<List<T>>

    abstract val snackBarMessage: MutableLiveData<Event<Int>>

    protected abstract val networkState: MutableLiveData<Result<List<T>>>

    open var isInternetAvailable: Boolean = true

    abstract fun getData(query: String = "")

}