package com.martdev.android.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.martdev.android.local.entity.VideoFileEntity

@Dao
interface VideoFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videoFiles: List<VideoFileEntity>)
}