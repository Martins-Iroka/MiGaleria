package com.martdev.domain.photodata

import kotlinx.coroutines.flow.Flow

class PhotoDataUseCase(
    val photoDataSource: PhotoDataSource
) {

    fun getPhotoDataById(id: Long): Flow<PhotoData> = photoDataSource.getPhotoDataById(id)

    fun getPhotoInfo(limit: Int, offset: Int) = photoDataSource.getPhotoInfo(limit, offset)

    fun loadPhotos(): Flow<List<PhotoUrlAndIdData>> = photoDataSource.loadLocalPhotos()

    suspend fun refreshPhotos() = photoDataSource.refreshPhotos()

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean) =
        photoDataSource.updateBookmarkStatus(photoId, isBookmarked)

    fun postComment(postId: String, content: String) = photoDataSource.postComment(postId, content)

    fun getCommentsByPostId(postId: String) = photoDataSource.getCommentsByPostID(postId)
}