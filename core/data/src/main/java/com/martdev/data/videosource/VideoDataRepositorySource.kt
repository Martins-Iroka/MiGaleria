package com.martdev.data.videosource

import com.martdev.domain.VideoDataInfo
import com.martdev.domain.VideoImageUrlAndIdData
import kotlinx.coroutines.flow.Flow

interface VideoDataRepositorySource {

    fun getVideoDataById(id: Long): Flow<VideoDataInfo>

    fun getVideoImageUrlAndId(): Flow<List<VideoImageUrlAndIdData>>

    suspend fun refreshOrSearchVideos(query: String)

    suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean): Int
}