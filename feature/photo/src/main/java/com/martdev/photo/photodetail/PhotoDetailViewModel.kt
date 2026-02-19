package com.martdev.photo.photodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoPostComments
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface PhotoDetailUiState {
    object Loading : PhotoDetailUiState
    data class Successful(val comments: List<PhotoPostComments>) : PhotoDetailUiState
    data class Error(val message: String) : PhotoDetailUiState
}

class PhotoDetailViewModel(
    postId: String,
    photoUseCase: PhotoDataUseCase
) : ViewModel() {

    val photoComments = photoUseCase.getCommentsByPostId(
        postId
    ).map {
        when(it) {
            is ResponseData.Error -> PhotoDetailUiState.Error(it.message)
            ResponseData.Loading -> PhotoDetailUiState.Loading
            ResponseData.NoResponse -> {}
            is ResponseData.Success<List<PhotoPostComments>> -> PhotoDetailUiState.Successful(it.data.orEmpty())
        }
    }.catch {
        emit(PhotoDetailUiState.Error(it.localizedMessage?: "An error occurred"))
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PhotoDetailUiState.Loading
    )
}