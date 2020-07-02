package com.martdev.android.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.martdev.android.local.entity.PhotoEntity

@Dao
interface PhotoDataDao {

    @Query("SELECT * FROM photo_data")
    fun getPhotoData(): DataSource.Factory<Int, PhotoEntity>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun savePhotoData(photoDataEntity: PhotoEntity)

    @Query("DELETE FROM photo_data")
    suspend fun deletePhotoData()
}