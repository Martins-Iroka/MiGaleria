package com.martdev.remote.photo

import com.martdev.remote.NetworkResult
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.photo.model.CreatePhotoCommentRequest
import com.martdev.remote.photo.model.CreatePhotoCommentResponse
import com.martdev.remote.photo.model.PhotoPostCommentResponse
import com.martdev.remote.photo.model.PhotoSrcAPI
import kotlinx.coroutines.flow.Flow

interface PhotoRemoteDataSource {

    fun getAllPhotoPosts(limit: Int, offset: Int): Flow<NetworkResult<ResponseDataPayload<List<PhotoSrcAPI>>>>

    fun postComment(postID: String, commentRequest: CreatePhotoCommentRequest): Flow<NetworkResult<ResponseDataPayload<CreatePhotoCommentResponse>>>

    fun getCommentsByPostID(postID: String): Flow<NetworkResult<ResponseDataPayload<List<PhotoPostCommentResponse>>>>
}