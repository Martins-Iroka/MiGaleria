package com.martdev.android.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.martdev.android.local.entity.PhotoSrcEntity

@Dao
interface PhotoSrcDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photoSrcEntity: PhotoSrcEntity)
}