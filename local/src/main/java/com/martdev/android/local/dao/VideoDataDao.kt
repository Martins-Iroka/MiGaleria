package com.martdev.android.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martdev.android.local.entity.VideoDataEntity

@Dao
interface VideoDataDao {

    @Query("SELECT * FROM video_data")
    fun getVideoData(): DataSource.Factory<Int, VideoDataEntity>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun saveVideoData(videoDataEntity: VideoDataEntity)

    @Query("DELETE FROM video_data")
    suspend fun deleteVideoData()
}