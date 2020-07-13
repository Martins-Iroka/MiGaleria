package com.martdev.android.data.repo

import com.martdev.android.data.*
import com.martdev.android.domain.Repository
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.PhotoDataSource
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoDataRepo(
    private val remoteDataSource: RemoteDataSource<PhotoData>,
    private val localDataSource: LocalDataSource<PhotoEntity, PhotoDataEntity>
) : Repository<Photo> {

    override suspend fun getData(query: String, networkConnected: Boolean): Result<List<Photo>> {
        return withContext(Dispatchers.IO) {
            fetchFromRemoteOrLocal(query, networkConnected)
        }
    }

    private suspend fun fetchFromRemoteOrLocal(
        query: String,
        networkConnected: Boolean
    ): Result<List<Photo>> {
        if (networkConnected) {
            val result = if (query.isEmpty()) {
                remoteDataSource.load()
            } else remoteDataSource.search(query)

            when (result.status) {
                Result.Status.LOADING -> Result.loading(null)
                Result.Status.SUCCESS -> {
                    val photos = result.data?.photos!!
                    refreshLocalDataSource(photos)
                    return Result.success(photos)
                }
                Result.Status.ERROR -> Result.error(result.message)
            }
        }
        val localResult = localDataSource.getData().data?.map {
            it.toPhoto()
        } ?: emptyList()
        return Result.success(localResult)
    }

    private suspend fun refreshLocalDataSource(
        photos: List<Photo>
    ) {
        localDataSource as PhotoDataSource
        localDataSource.deleteData()
        photos.forEach { photo ->
            localDataSource.saveData(photo.toPhotoEntity())
            localDataSource.savePhotoSrc(photo.src.toSrcEntity(photo.id))
        }
    }
}