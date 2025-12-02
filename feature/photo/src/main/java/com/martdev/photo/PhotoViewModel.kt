package com.martdev.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val photoUseCase: PhotoDataUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<ResponseData<List<PhotoData>>>(
        ResponseData.NoResponse
    )

    val photos = _photos.asStateFlow()

    fun getPhotos(limit: Int, offset: Int) {
        viewModelScope.launch {
            photoUseCase.getPhotos(limit, offset)
                .onStart {
                    _photos.value = ResponseData.Loading
                }.catch {
                    _photos.value = ResponseData.Error(it.message.orEmpty().ifEmpty { "error occurred" })
                }.collect {
                    _photos.value = it
                }
        }
    }
}