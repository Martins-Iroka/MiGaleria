package com.martdev.android.local

import androidx.paging.DataSource
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
    override fun getData(): DataSource.Factory<Int, VideoDataEntity> {
        return videoData.getVideoData()
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