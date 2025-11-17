package com.martdev.data.photosource

import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.data.util.toPhotoUrlAndIdData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoUrlAndIdData
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.photo.model.PhotoPostResponsePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class PhotoDataRepositoryImpl(
    private val localPhotoSource: PhotoLocalDataSource,
    private val remoteSource: RemoteDataSource<PhotoPostResponsePayload>
) : PhotoDataSource {
    override fun getPhotoDataById(id: Long): Flow<PhotoData> {
        return localPhotoSource.getPhotoEntityById(id).map { it.toPhotoData() }
    }

    override fun loadPhotos(): Flow<List<PhotoUrlAndIdData>> {
        return localPhotoSource.getPhotoURLAndID().map {
            it.toPhotoUrlAndIdData()
        }
    }

    override suspend fun refreshPhotos() {
        localPhotoSource.deletePhotoEntity()
        val remotePhotos = remoteSource.load().firstOrNull()
        remotePhotos?.let { localPhotoSource.savePhotoEntity(it.toPhotoEntity()) }
    }

    override suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int {
        return localPhotoSource.updateBookmarkStatus(photoId, isBookmarked)
    }
}