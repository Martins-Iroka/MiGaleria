package com.martdev.domain.videodata

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

interface VideoDataSource {

    fun getVideoDataById(id: Long): Flow<VideoData>

    fun getVideoImageUrlAndId(): Flow<List<VideoImageUrlAndIdData>>

    fun getVideoPosts(limit: Int, offset: Int): Flow<ResponseData<VideoPost>>

    suspend fun refreshVideos()

    suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean): Int

    fun postComment(postId: String, content: String): Flow<ResponseData<Nothing>>

    fun getCommentsByPostID(postId: String): Flow<ResponseData<List<VideoPostComments>>>
}