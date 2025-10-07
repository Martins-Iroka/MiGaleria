package com.martdev.data.photosource

import com.martdev.data.PhotoDataRepositorySource
import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.data.util.toPhotoUrlAndIdData
import com.martdev.domain.PhotoDataClass
import com.martdev.domain.PhotoUrlAndIdData
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.remotephoto.PhotoDataAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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
            it.toPhotoUrlAndIdData()
        }
    }

    override suspend fun refreshOrSearchPhotos(query: String) {
        localPhotoSource.deletePhotoEntity()
        val remotePhotos = if (query.isEmpty()) remoteSource.load().firstOrNull() else remoteSource.search(query).firstOrNull()
        remotePhotos?.let { localPhotoSource.savePhotoEntity(it.toPhotoEntity()) }
    }

    override suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int {
        return localPhotoSource.updateBookmarkStatus(photoId, isBookmarked)
    }
}