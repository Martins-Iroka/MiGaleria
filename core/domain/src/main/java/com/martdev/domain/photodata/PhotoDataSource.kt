package com.martdev.domain.photodata

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

interface PhotoDataSource {

    fun getPhotoDataById(id: Long): Flow<PhotoData>

    fun loadLocalPhotos(): Flow<List<PhotoUrlAndIdData>>

    suspend fun refreshPhotos()

    fun getPhotoInfo(limit: Int, offset: Int): Flow<ResponseData<PhotoInfo>>

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int

    fun postComment(postId: String, content: String): Flow<ResponseData<Nothing>>

    fun getCommentsByPostID(postId: String): Flow<ResponseData<List<PhotoPostComments>>>
}