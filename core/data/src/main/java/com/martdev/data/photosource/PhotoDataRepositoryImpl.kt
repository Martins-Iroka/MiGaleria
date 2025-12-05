package com.martdev.data.photosource

import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.data.util.toPhotoUrlAndIdData
import com.martdev.data.util.toResponseData
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.CreatePhotoCommentData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoInfo
import com.martdev.domain.photodata.PhotoPostComments
import com.martdev.domain.photodata.PhotoUrlAndIdData
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.photo.PhotoRemoteDataSource
import com.martdev.remote.photo.model.CreatePhotoCommentRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoDataRepositoryImpl(
    private val localPhotoSource: PhotoLocalDataSource,
    private val remoteSource: PhotoRemoteDataSource
) : PhotoDataSource {
    override fun getPhotoDataById(id: Long): Flow<PhotoData> {
        return localPhotoSource.getPhotoEntityById(id).map {
            it.toPhotoData() }
    }

    override fun getPhotos(limit: Int, offset: Int): Flow<ResponseData<List<PhotoData>>> {
        return remoteSource.getAllPhotoPosts(limit, offset)
            .map {
                it.toResponseData { res ->
                    localPhotoSource.savePhotoEntity(res.data.toPhotoEntity())
                    res.data.toPhotoData()
                }
            }
    }

    override fun getPhotoInfo(limit: Int, offset: Int): Flow<ResponseData<PhotoInfo>> {
        return remoteSource.getPhotoPosts(limit, offset)
            .map {
                it.toResponseData { res ->
                    localPhotoSource.savePhotoEntity(res.data.photoItems.toPhotoEntity())
                    PhotoInfo(res.data.photoItems.toPhotoData(), res.data.nextPage)
                }
            }
    }

    override fun loadLocalPhotos(): Flow<List<PhotoUrlAndIdData>> {
        return localPhotoSource.getPhotoURLAndID().map {
            it.toPhotoUrlAndIdData()
        }
    }

    override suspend fun refreshPhotos() {
        /*localPhotoSource.deletePhotoEntity()
        val remotePhotos = remoteSource.load().firstOrNull()
        remotePhotos?.let { localPhotoSource.savePhotoEntity(it.toPhotoEntity()) }*/
    }

    override suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int {
        return localPhotoSource.updateBookmarkStatus(photoId, isBookmarked)
    }

    override fun postComment(
        postId: String,
        commentData: CreatePhotoCommentData
    ): Flow<ResponseData<Nothing>> {
        return remoteSource.postComment(
            postId,
            CreatePhotoCommentRequest(
                commentData.userID,
                commentData.content
            )).map {
                it.toResponseData()
        }
    }

    override fun getCommentsByPostID(postId: String): Flow<ResponseData<List<PhotoPostComments>>> {
        return remoteSource.getCommentsByPostID(postId)
            .map { res ->
                res.toResponseData { success ->
                    success.data.map {
                        PhotoPostComments(
                            it.content,
                            it.createdAt,
                            it.username
                        )
                    }
                }
            }
    }
}