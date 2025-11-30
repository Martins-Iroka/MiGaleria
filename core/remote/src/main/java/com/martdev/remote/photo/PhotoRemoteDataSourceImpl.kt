package com.martdev.remote.photo

import com.martdev.common.NetworkResult
import com.martdev.remote.client.CREATE_PHOTOS_COMMENT_PATH
import com.martdev.remote.client.Client
import com.martdev.remote.client.PHOTOS_PATH
import com.martdev.remote.client.PHOTO_COMMENTS_PATH
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.photo.model.CreatePhotoCommentRequest
import com.martdev.remote.photo.model.CreatePhotoCommentResponse
import com.martdev.remote.photo.model.PhotoPostCommentResponse
import com.martdev.remote.photo.model.PhotoSrcAPI
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotoRemoteDataSourceImpl(
    private val client: Client
) : PhotoRemoteDataSource{

    override fun getAllPhotoPosts(limit: Int, offset: Int): Flow<NetworkResult<ResponseDataPayload<List<PhotoSrcAPI>>>> {
        return flow {
            val result =
                client.getRequest<ResponseDataPayload<List<PhotoSrcAPI>>>(PHOTOS_PATH) {
                    parameter("limit", limit)
                    parameter("offset", offset)
                }
            emit(result)
        }
    }

    override fun postComment(
        postID: String,
        commentRequest: CreatePhotoCommentRequest
    ): Flow<NetworkResult<ResponseDataPayload<CreatePhotoCommentResponse>>> {
        return flow {
            val result =
                client.postData<CreatePhotoCommentRequest,ResponseDataPayload<CreatePhotoCommentResponse>>(
                    CREATE_PHOTOS_COMMENT_PATH.replace("{postID}", postID),
                    body = commentRequest
                )

            emit(result)
        }
    }

    override fun getCommentsByPostID(postID: String): Flow<NetworkResult<ResponseDataPayload<List<PhotoPostCommentResponse>>>> {
        return flow {
            val result =
                client.getRequest<ResponseDataPayload<List<PhotoPostCommentResponse>>>(
                    urlString = PHOTO_COMMENTS_PATH.replace("{postID}", postID)
                )

            emit(result)
        }
    }
}