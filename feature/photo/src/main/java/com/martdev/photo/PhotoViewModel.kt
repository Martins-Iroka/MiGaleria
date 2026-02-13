package com.martdev.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoPostComments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val photoUseCase: PhotoDataUseCase
) : ViewModel() {

    private val _photoComments = MutableStateFlow<ResponseData<List<PhotoPostComments>>>(
        ResponseData.NoResponse
    )
    val photoComments = _photoComments.asStateFlow()

    private val _createCommentsResponse = MutableStateFlow<ResponseData<Nothing>>(ResponseData.NoResponse)
    val createCommentsResponse = _createCommentsResponse.asStateFlow()

    val photoList = Pager(
        PagingConfig(
            pageSize = 20
        )
    ) {
        PhotoPagingSource(photoUseCase)
    }.flow.cachedIn(viewModelScope)

    fun getCommentsByPostId(postId: String) {
        viewModelScope.launch {
            photoUseCase.getCommentsByPostId(postId = postId)
                .onStart {
                    _photoComments.value = ResponseData.Loading
                }.catch {
                    _photoComments.value = ResponseData.Error(it.localizedMessage?: "An error occurred")
                }.collect {
                    _photoComments.value = it
                }
        }
    }

    fun postComment(postId: String, content: String) {
        viewModelScope.launch {
            photoUseCase.postComment(postId, content)
                .onStart {
                    _createCommentsResponse.value = ResponseData.Loading
                }.catch {
                    _createCommentsResponse.value = ResponseData.Error(it.localizedMessage?: "An error occurred")
                }.collect {
                    _createCommentsResponse.value = it
                    if (it is ResponseData.Success) {
                        getCommentsByPostId(postId)
                    }
                }
        }
    }
}