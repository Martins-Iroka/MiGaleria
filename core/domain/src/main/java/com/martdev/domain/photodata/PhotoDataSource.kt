package com.martdev.domain.photodata

import kotlinx.coroutines.flow.Flow

interface PhotoDataSource {

    fun getPhotoDataById(id: Long): Flow<PhotoData>

    fun loadPhotos(): Flow<List<PhotoUrlAndIdData>>

    suspend fun refreshOrSearchPhotos(query: String)

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int
}