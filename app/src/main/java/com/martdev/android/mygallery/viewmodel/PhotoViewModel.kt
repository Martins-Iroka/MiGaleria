package com.martdev.android.mygallery.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.martdev.android.data.SourceResult
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData

class PhotoViewModel(private val photoUseCase: UseCase<Photo>) : BaseViewModel<Photo>() {

    override val _searchKeyword: MutableLiveData<String> = MutableLiveData()

    override val searchKeyword: LiveData<String>
        get() = _searchKeyword

    override val result: LiveData<SourceResult<Photo>>
        get() = Transformations.map(_searchKeyword) {
            photoUseCase(it, viewModelScope, isNetworkAvailable)
        }

    override val data: LiveData<PagedList<Photo>>
        get() = Transformations.switchMap(result) {
        it.data
    }

    override val networkState: LiveData<Result<List<Photo>>>
        get() = Transformations.switchMap(result) {
            it.networkState
        }

    override fun search(query: String) {
        _searchKeyword.value = query
    }
}