package com.martdev.android.data.repo

import android.util.Log
import com.martdev.android.data.*
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.PhotoDataSource
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.remotephoto.PhotoRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class PhotoDataRepo(
    private val remoteDataSource: RemoteDataSource<PhotoData>,
    private val localDataSource: LocalDataSource<PhotoEntity, PhotoDataEntity>
) : Repository<Photo>, AnkoLogger {

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

            info {
                result.data?.photos.toString()
            }
            when (result.status) {
                Result.Status.LOADING -> Result.loading(null)
                Result.Status.SUCCESS -> {
                    val photos = result.data?.photos!!
                    refreshLocalDataSource(photos)
                    return Result.success(photos)
                }
                Result.Status.ERROR -> {
                    error {
                        result.message
                    }
                    Result.error(result.message)}
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

//    fun getData(
//        query: String?,
//        scope: CoroutineScope,
//        networkConnected: Boolean
//    ): SourceResult<Photo> {
//        return if (networkConnected) getRemotePhotoData(query, scope) else getLocalPhotoData()
//    }
//
//    private fun getRemotePhotoData(query: String?, scope: CoroutineScope): SourceResult<Photo> {
//        val factory = PhotoPageDataSourceFactory(query, localDataSource, remoteDataSource, scope)
//
//        val pageList =
//            LivePagedListBuilder(factory, pagedListConfig()).build()
//
//        val networkState = Transformations.switchMap(factory.liveData) {
//            it.networkState
//        }
//
//        val retryCallback = factory.liveData.value?.retry
//
//        return SourceResult(pageList, networkState, retryCallback)
//    }
//
//    private fun getLocalPhotoData(): SourceResult<Photo> {
//        val dataSource =
//            localDataSource.getData().map {
//                it.toPhoto()
//            }
//
//        val pageList =
//            LivePagedListBuilder(dataSource, pagedListConfig()).build()
//
//        return SourceResult(pageList)
//    }
}