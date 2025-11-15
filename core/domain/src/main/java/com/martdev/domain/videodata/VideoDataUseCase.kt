package com.martdev.domain.videodata

import kotlinx.coroutines.flow.Flow

class VideoDataUseCase(
    private val videoDataSource: VideoDataSource
) {

    fun getVideoDataById(id: Long): Flow<VideoData> = videoDataSource.getVideoDataById(id)

    fun getVideoImageUrlAndId() = videoDataSource.getVideoImageUrlAndId()

    suspend fun refreshOrSearchVideos() = videoDataSource.refreshVideos()

    suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean) =
        videoDataSource.updateBookmarkStatus(videoId, isBookmarked)
}