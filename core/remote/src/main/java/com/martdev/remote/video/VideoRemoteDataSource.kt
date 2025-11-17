package com.martdev.remote.video

import com.martdev.remote.NetworkResult
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.video.model.CreateVideoCommentRequest
import com.martdev.remote.video.model.CreateVideoCommentResponse
import com.martdev.remote.video.model.VideoPostCommentResponse
import com.martdev.remote.video.model.VideoPostResponse
import kotlinx.coroutines.flow.Flow

interface VideoRemoteDataSource {

    fun getAllVideoPosts(limit: Int, offset: Int): Flow<NetworkResult<ResponseDataPayload<List<VideoPostResponse>>>>

    fun postComment(postID: String, commentRequest: CreateVideoCommentRequest):
            Flow<NetworkResult<ResponseDataPayload<CreateVideoCommentResponse>>>

    fun getCommentsByPostID(postID: String):
            Flow<NetworkResult<ResponseDataPayload<List<VideoPostCommentResponse>>>>
}