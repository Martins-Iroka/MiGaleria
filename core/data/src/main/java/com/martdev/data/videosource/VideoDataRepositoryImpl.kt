package com.martdev.data.videosource

import com.martdev.data.util.toResponseData
import com.martdev.data.util.toVideoDataInfo
import com.martdev.data.util.toVideoImageUrlAndIdData
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataSource
import com.martdev.domain.videodata.VideoFileData
import com.martdev.domain.videodata.VideoImageUrlAndIdData
import com.martdev.domain.videodata.VideoPost
import com.martdev.domain.videodata.VideoPostComments
import com.martdev.local.videodatasource.VideoLocalDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.user.UserStorage
import com.martdev.remote.video.VideoRemoteDataSource
import com.martdev.remote.video.model.CreateVideoCommentRequest
import com.martdev.remote.video.model.CreateVideoCommentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class VideoDataRepositoryImpl(
    private val localDataSource: VideoLocalDataSource,
    private val remoteSource: VideoRemoteDataSource,
    private val userStorage: UserStorage
) : VideoDataSource {

    override fun getVideoDataById(id: Long): Flow<VideoData> {
        return localDataSource.getVideoEntityByID(id).map {
            it.toVideoDataInfo()
        }
    }

    override fun getVideoImageUrlAndId(): Flow<List<VideoImageUrlAndIdData>> {
        return localDataSource.getVideoImageURLAndID().map {
            it.toVideoImageUrlAndIdData()
        }
    }

    override fun getVideoPosts(
        limit: Int,
        offset: Int
    ): Flow<ResponseData<VideoPost>> {
        return remoteSource.getVideoPosts(limit, offset).map {
            it.toResponseData { res ->
                VideoPost(
                    videoItems = res.data.videoItems.map { v ->
                        VideoData(
                            id = v.id,
                            videoImage = v.videoImage,
                            videoUrl = v.videoUrl,
                            duration = v.duration,
                            videoFiles = v.videoFiles.map { vf ->
                                VideoFileData(
                                    link = vf.videoLink,
                                    size = vf.videoSize
                                )
                            }
                        )
                    }
                )
            }
        }
    }

    override suspend fun refreshVideos() {
      //nothing here
    }

    override suspend fun updateBookmarkStatus(
        videoId: Long,
        isBookmarked: Boolean
    ): Int {
        return localDataSource.updateBookmarkStatus(videoId, isBookmarked)
    }

    override fun postComment(
        postId: String,
        content: String
    ): Flow<ResponseData<Nothing>> {
        return flow {
            val userId = userStorage.getUserData().firstOrNull()?.userId ?: throw IllegalStateException("no user id found")

            val r = remoteSource.postComment(
                postId,
                CreateVideoCommentRequest(
                    userId,
                    content
                )
            ).first()

            emit(r.toResponseData<ResponseDataPayload<CreateVideoCommentResponse>, Nothing>())
        }.catch {
            emit(ResponseData.Error(it.message ?: "An error occurred"))
        }
    }

    override fun getCommentsByPostID(postId: String): Flow<ResponseData<List<VideoPostComments>>> {
        return remoteSource.getCommentsByPostID(postId)
            .map { result ->
                result.toResponseData { res ->
                    res.data.map {
                        VideoPostComments(
                            content = it.content,
                            createdAt = it.createdAt,
                            username = it.username,
                            id = it.id
                        )
                    }
                }

            }
    }
}