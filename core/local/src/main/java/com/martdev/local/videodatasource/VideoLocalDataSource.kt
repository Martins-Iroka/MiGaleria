package com.martdev.local.videodatasource

import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
import com.martdev.local.entity.VideoImageUrlAndID
import kotlinx.coroutines.flow.Flow

interface VideoLocalDataSource {

    suspend fun deleteVideoEntity()

    fun getVideoEntityByID(id: Long): Flow<Map<VideoEntity, List<VideoFileEntity>>>

    fun getVideoImageURLAndID(): Flow<List<VideoImageUrlAndID>>

    suspend fun saveVideoEntity(videoEntity: List<VideoEntity>): List<Long>

    suspend fun saveVideoFiles(videoFileEntity: List<VideoFileEntity>): List<Long>

    suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean): Int
}