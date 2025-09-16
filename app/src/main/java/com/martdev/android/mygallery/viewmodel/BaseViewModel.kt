package com.martdev.android.mygallery.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.mygallery.utils.Event

abstract class BaseViewModel<T> : ViewModel() {

 /*   abstract val searchKeyword: MutableLiveData<String>

    abstract val data: MutableLiveData<List<T>>

    abstract val snackBarMessage: MutableLiveData<Event<Any>>

    abstract val loading: LiveData<Event<Boolean>>

    abstract val downloadProgress: LiveData<Event<Int>>

    abstract val fileUri: LiveData<Event<Uri>>

    abstract val fileName: LiveData<Event<String>>

    abstract val byteArray: LiveData<Event<ByteArray>>

    open var isInternetAvailable: Boolean = true

    abstract fun getData(query: String = "")*/

}