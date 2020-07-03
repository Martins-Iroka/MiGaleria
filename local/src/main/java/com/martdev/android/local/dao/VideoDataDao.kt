package com.martdev.android.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.local.entity.VideoEntity

@Dao
interface VideoDataDao {

    @Transaction
    @Query("SELECT * FROM video_data")
    fun getVideoData(): DataSource.Factory<Int, VideoDataEntity>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun saveVideoData(videoEntity: VideoEntity)

    @Query("DELETE FROM video_data")
    suspend fun deleteVideoData()
}