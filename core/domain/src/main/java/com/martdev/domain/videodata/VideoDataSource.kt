package com.martdev.domain.videodata

import kotlinx.coroutines.flow.Flow

interface VideoDataSource {

    fun getVideoDataById(id: Long): Flow<VideoData>

    fun getVideoImageUrlAndId(): Flow<List<VideoImageUrlAndIdData>>

    suspend fun refreshOrSearchVideos(query: String)

    suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean): Int
}