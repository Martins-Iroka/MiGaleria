package com.martdev.data

import com.martdev.domain.PhotoDataClass
import com.martdev.domain.PhotoUrlAndIdData
import kotlinx.coroutines.flow.Flow

interface PhotoDataRepositorySource {

    fun getPhotoDataById(id: Long): Flow<PhotoDataClass>

    fun loadPhotos(): Flow<List<PhotoUrlAndIdData>>

    suspend fun refreshOrSearchPhotos(query: String)

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int
}