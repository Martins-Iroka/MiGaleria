package com.martdev.data.photosource

import com.martdev.data.PhotoDataRepositorySource
import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.domain.PhotoDataClass
import com.martdev.domain.PhotoUrlAndIdData
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.remotephoto.PhotoDataAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PhotoDataRepositoryImpl(
    private val localPhotoSource: PhotoLocalDataSource,
    private val remoteSource: RemoteDataSource<PhotoDataAPI>
) : PhotoDataRepositorySource {
    override fun getPhotoDataById(id: Long): Flow<PhotoDataClass> {
        return localPhotoSource.getPhotoEntityById(id).map { it.toPhotoData() }
    }

    override fun loadPhotos(): Flow<List<PhotoUrlAndIdData>> {
        return localPhotoSource.getPhotoURLAndID().map {
            it.map { (photoId, original) ->
                PhotoUrlAndIdData(photoId, original)
            }
        }
    }

    override suspend fun refreshOrSearchPhotos(query: String) {
        localPhotoSource.deletePhotoEntity()
        val remotePhotos = if (query.isEmpty()) remoteSource.load().first() else remoteSource.search(query).first()
        localPhotoSource.savePhotoEntity(remotePhotos.toPhotoEntity())
    }

    override suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int {
        return localPhotoSource.updateBookmarkStatus(photoId, isBookmarked)
    }
}