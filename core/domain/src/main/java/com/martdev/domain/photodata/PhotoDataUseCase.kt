package com.martdev.domain.photodata

import kotlinx.coroutines.flow.Flow

class PhotoDataUseCase(
    private val photoDataSource: PhotoDataSource
) {

    fun getPhotoDataById(id: Long): Flow<PhotoData> = photoDataSource.getPhotoDataById(id)

    fun getPhotos(limit: Int, offset: Int) = photoDataSource.getPhotos(limit, offset)

    fun loadPhotos(): Flow<List<PhotoUrlAndIdData>> = photoDataSource.loadLocalPhotos()

    suspend fun refreshPhotos() = photoDataSource.refreshPhotos()

    suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean) =
        photoDataSource.updateBookmarkStatus(photoId, isBookmarked)

    fun postComment(postId: String, commentData: CreatePhotoCommentData) = photoDataSource.postComment(postId, commentData)

    fun getCommentsByPostId(postId: String) = photoDataSource.getCommentsByPostID(postId)
}