package com.martdev.domain.photodata

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

interface PhotoDataSource {

    fun getPhotoDataById(id: Long): Flow<PhotoData>

    fun loadLocalPhotos(): Flow<List<PhotoUrlAndIdData>>

    suspend fun refreshPhotos()

    fun getPhotos(limit: Int, offset: Int): Flow<ResponseData<List<PhotoData>>>

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int
}