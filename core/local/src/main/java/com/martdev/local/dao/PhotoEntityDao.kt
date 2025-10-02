package com.martdev.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.entity.PhotoUrlAndID
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoEntityDao {

    @Query("DELETE FROM photo_data")
    suspend fun deletePhotoEntity(): Int

    @Query("SELECT * FROM photo_data WHERE photoId = :id")
    fun getPhotoEntityById(id: Long): Flow<PhotoEntity>

    @Query("SELECT photoId, original FROM photo_data")
    fun getPhotoURLAndID(): Flow<List<PhotoUrlAndID>>

    @Insert(onConflict = REPLACE)
    suspend fun savePhotoEntity(photoEntity: List<PhotoEntity>): List<Long>

    @Query("UPDATE photo_data SET bookmarked = :isBookmarked WHERE photoId = :photoId")
    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int

}