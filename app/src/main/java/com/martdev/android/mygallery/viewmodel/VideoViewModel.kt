package com.martdev.android.mygallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.martdev.android.data.SourceResult
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video

class VideoViewModel(private val videoUseCase: UseCase<Video>) : BaseViewModel<Video>() {

    override val _searchKeyword: MutableLiveData<String> = MutableLiveData()

    var isConnected = true
    override val result: LiveData<SourceResult<Video>> = Transformations.map(_searchKeyword) {
            videoUseCase(it, viewModelScope, isConnected)
        }
    override val data: LiveData<PagedList<Video>> = Transformations.switchMap(result) {
            it.data
        }
    override val networkState: LiveData<Result<List<Video>>> = Transformations.switchMap(result) {
            it.networkState
        }

    override fun search(query: String?) {
        _searchKeyword.value = query
    }
}