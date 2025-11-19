package com.martdev.remote.video

import com.martdev.common.NetworkResult
import com.martdev.remote.CREATE_VIDEOS_COMMENT_PATH
import com.martdev.remote.Client
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.VIDEOS_PATH
import com.martdev.remote.VIDEO_COMMENTS_PATH
import com.martdev.remote.video.model.CreateVideoCommentRequest
import com.martdev.remote.video.model.CreateVideoCommentResponse
import com.martdev.remote.video.model.VideoPostCommentResponse
import com.martdev.remote.video.model.VideoPostResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRemoteDataSourceImpl(
    private val client: Client
) : VideoRemoteDataSource {

    override fun getAllVideoPosts(limit: Int, offset: Int): Flow<NetworkResult<ResponseDataPayload<List<VideoPostResponse>>>> {
        return flow {
            val result =
                client.getRequest<ResponseDataPayload<List<VideoPostResponse>>>(VIDEOS_PATH)
            emit(result)
        }
    }

    override fun postComment(
        postID: String,
        commentRequest: CreateVideoCommentRequest
    ): Flow<NetworkResult<ResponseDataPayload<CreateVideoCommentResponse>>> {
        return flow {
            val result =
                client.postData<CreateVideoCommentRequest, ResponseDataPayload<CreateVideoCommentResponse>>(
                    urlString = CREATE_VIDEOS_COMMENT_PATH.replace("{postID}", postID),
                    commentRequest
                )
            emit(result)
        }
    }

    override fun getCommentsByPostID(postID: String): Flow<NetworkResult<ResponseDataPayload<List<VideoPostCommentResponse>>>> {
        return flow {
            val result =
                client.getRequest<ResponseDataPayload<List<VideoPostCommentResponse>>>(
                    urlString = VIDEO_COMMENTS_PATH.replace("{postID}", postID)
                )
            emit(result)
        }
    }
}