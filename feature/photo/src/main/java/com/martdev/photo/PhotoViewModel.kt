package com.martdev.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.martdev.domain.photodata.PhotoDataUseCase

class PhotoViewModel(
    private val photoUseCase: PhotoDataUseCase
) : ViewModel() {
    val photoList = Pager(
        PagingConfig(
            pageSize = 20
        )
    ) {
        PhotoPagingSource(photoUseCase)
    }.flow.cachedIn(viewModelScope)
}