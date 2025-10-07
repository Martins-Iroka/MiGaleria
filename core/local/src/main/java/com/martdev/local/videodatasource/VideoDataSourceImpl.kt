package com.martdev.local.videodatasource

import com.martdev.local.dao.VideoEntityDao
import com.martdev.local.doIOOperation
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
import com.martdev.local.entity.VideoImageUrlAndID
import kotlinx.coroutines.flow.Flow

class VideoDataSourceImpl(
    private val videoEntityDao: VideoEntityDao
) : VideoDataSource{
    override suspend fun deleteVideoEntity() {
        doIOOperation { videoEntityDao.deleteVideoEntity() }
    }

    override fun getVideoEntityByID(id: Long): Flow<Map<VideoEntity, List<VideoFileEntity>>> {
        return videoEntityDao.getVideoEntityByID(id)
    }

    override fun getVideoImageURLAndID(): Flow<List<VideoImageUrlAndID>> {
        return videoEntityDao.getVideoImageURLAndID()
    }

    override suspend fun saveVideoEntity(videoEntity: List<VideoEntity>): List<Long> {
        return doIOOperation { videoEntityDao.saveVideoEntity(videoEntity) }
    }

    override suspend fun saveVideoFiles(videoFileEntity: List<VideoFileEntity>): List<Long> {
        return doIOOperation { videoEntityDao.saveVideoFiles(videoFileEntity) }
    }

    override suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean): Int {
        return doIOOperation {
            videoEntityDao.updateBookmarkStatus(videoId, isBookmarked)
        }
    }
}