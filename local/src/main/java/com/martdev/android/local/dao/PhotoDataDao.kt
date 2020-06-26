package com.martdev.android.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.martdev.android.local.entity.PhotoDataEntity

@Dao
interface PhotoDataDao {

    @Query("SELECT * FROM photo_data")
    fun getPhotoData(): DataSource.Factory<Int, PhotoDataEntity>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun savePhotoData(photoDataEntity: PhotoDataEntity)

    @Query("DELETE FROM photo_data")
    suspend fun deletePhotoData()
}