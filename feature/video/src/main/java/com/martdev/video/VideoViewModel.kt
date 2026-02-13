package com.martdev.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoDataUseCase
import com.martdev.domain.videodata.VideoPostComments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VideoViewModel(
    private val videoDataUseCase: VideoDataUseCase
) : ViewModel() {

    private val _videoComments = MutableStateFlow<ResponseData<List<VideoPostComments>>>(
        ResponseData.NoResponse
    )
    val videoComments = _videoComments.asStateFlow()

    private val _createCommentResponse = MutableStateFlow<ResponseData<Nothing>>(
        ResponseData.NoResponse
    )
    val createCommentResponse = _createCommentResponse.asStateFlow()

    val videoList = Pager(
        PagingConfig(
            pageSize = 20
        )
    ) {
        VideoPagingSource(videoDataUseCase)
    }.flow.cachedIn(viewModelScope)

    fun getCommentsByPostId(postId: String) {
        viewModelScope.launch {
            videoDataUseCase.getCommentsByPostId(postId = postId)
                .onStart {
                    _videoComments.value = ResponseData.Loading
                }.catch {
                    _videoComments.value = ResponseData.Error(it.localizedMessage?: "An error occurred")
                }.collect {
                    _videoComments.value = it
                }
        }
    }

    fun postComment(postId: String, content: String) {
        viewModelScope.launch {
            videoDataUseCase.postComment(postId = postId, content = content)
                .onStart {
                    _createCommentResponse.value = ResponseData.Loading
                }.catch {
                    _createCommentResponse.value = ResponseData.Error(it.localizedMessage?: "An error occurred")
                }.collect {
                    _createCommentResponse.value = it
                    if (it is ResponseData.Success) {
                        getCommentsByPostId(postId)
                    }
                }
        }
    }
}