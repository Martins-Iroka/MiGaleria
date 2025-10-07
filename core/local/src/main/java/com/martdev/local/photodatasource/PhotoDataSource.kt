package com.martdev.local.photodatasource

import com.martdev.local.entity.PhotoEntity
import com.martdev.local.entity.PhotoUrlAndID
import kotlinx.coroutines.flow.Flow

interface PhotoDataSource {

    suspend fun deletePhotoEntity(): Int

    fun getPhotoEntityById(id: Long): Flow<PhotoEntity>

    fun getPhotoURLAndID(): Flow<List<PhotoUrlAndID>>

    suspend fun savePhotoEntity(photoEntity: List<PhotoEntity>): List<Long>

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int
}