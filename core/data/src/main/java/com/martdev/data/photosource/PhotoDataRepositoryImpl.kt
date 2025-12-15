package com.martdev.data.photosource

import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.data.util.toPhotoUrlAndIdData
import com.martdev.data.util.toResponseData
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoInfo
import com.martdev.domain.photodata.PhotoPostComments
import com.martdev.domain.photodata.PhotoUrlAndIdData
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.user.UserStorage
import com.martdev.remote.photo.PhotoRemoteDataSource
import com.martdev.remote.photo.model.CreatePhotoCommentRequest
import com.martdev.remote.photo.model.CreatePhotoCommentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PhotoDataRepositoryImpl(
    private val localPhotoSource: PhotoLocalDataSource,
    private val remoteSource: PhotoRemoteDataSource,
    private val userStorage: UserStorage
) : PhotoDataSource {
    override fun getPhotoDataById(id: Long): Flow<PhotoData> {
        return localPhotoSource.getPhotoEntityById(id).map {
            it.toPhotoData() }
    }

    override fun getPhotoInfo(limit: Int, offset: Int): Flow<ResponseData<PhotoInfo>> {
        return remoteSource.getAllPhotoPosts(limit, offset)
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

    override fun postComment(postId: String, content: String): Flow<ResponseData<Nothing>> {
        return flow {
            val userId = userStorage.getUserData().firstOrNull()?.userId ?: throw IllegalStateException("no user id found")
            val r = remoteSource.postComment(
                postId,
                CreatePhotoCommentRequest(
                    userId,
                    content
                )).first()

            emit(r.toResponseData<ResponseDataPayload<CreatePhotoCommentResponse>, Nothing>())
        }.catch {
            emit(ResponseData.Error(it.message ?: "An error occurred"))
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
                            it.username,
                            it.id
                        )
                    }
                }
            }
    }
}