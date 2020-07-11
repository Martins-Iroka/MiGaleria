package com.martdev.android.local

import com.martdev.android.domain.Result
import com.martdev.android.local.dao.UserDao
import com.martdev.android.local.dao.VideoDataDao
import com.martdev.android.local.dao.VideoFileDao
import com.martdev.android.local.entity.UserEntity
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.local.entity.VideoEntity
import com.martdev.android.local.entity.VideoFileEntity

class VideoDataSource(
    private val videoData: VideoDataDao,
    private val userDao: UserDao,
    private val videoFileDao: VideoFileDao
) : LocalDataSource<VideoEntity, VideoDataEntity> {
    override suspend fun getData(): Result<List<VideoDataEntity>> {
        return try {
            Result.success(videoData.getVideoData())
        } catch (e: Exception) {
            Result.error(e.message)
        }
    }

    override suspend fun saveData(data: VideoEntity) {
        videoData.saveVideoData(data)
    }

    suspend fun saveUser(data: UserEntity) {
        userDao.insert(data)
    }

    suspend fun saveVideoFile(data: List<VideoFileEntity>) {
        videoFileDao.insertAll(data)
    }

    override suspend fun deleteData() {
        videoData.deleteVideoData()
    }
}