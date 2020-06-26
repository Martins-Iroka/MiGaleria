package com.martdev.android.local

import androidx.paging.DataSource
import com.martdev.android.local.dao.VideoDataDao
import com.martdev.android.local.entity.VideoDataEntity

class VideoDataSource(private val videoData: VideoDataDao) : LocalDataSource<VideoDataEntity> {
    override fun getData(): DataSource.Factory<Int, VideoDataEntity> {
        return videoData.getVideoData()
    }

    override suspend fun saveData(data: VideoDataEntity) {
        videoData.saveVideoData(data)
    }

    override suspend fun deleteData() {
        videoData.deleteVideoData()
    }
}