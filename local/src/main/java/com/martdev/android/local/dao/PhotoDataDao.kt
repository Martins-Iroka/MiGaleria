package com.martdev.android.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.local.entity.PhotoDataEntity

@Dao
interface PhotoDataDao {

    @Transaction
    @Query("SELECT * FROM photo_data")
    fun getPhotoData(): DataSource.Factory<Int, PhotoDataEntity>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun savePhotoData(photoDataEntity: PhotoEntity)

    @Query("DELETE FROM photo_data")
    suspend fun deletePhotoData()
}