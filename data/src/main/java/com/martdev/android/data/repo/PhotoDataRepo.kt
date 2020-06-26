package com.martdev.android.data.repo

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.martdev.android.data.Repository
import com.martdev.android.data.SourceResult
import com.martdev.android.data.pagedListConfig
import com.martdev.android.data.pagingfactory.PhotoPageDataSourceFactory
import com.martdev.android.data.toPhoto
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope

class PhotoDataRepo(
    private val remoteDataSource: RemoteDataSource<PhotoData>,
    private val localDataSource: LocalDataSource<PhotoDataEntity>
) : Repository<PhotoData>{

    override fun getData(
        query: String?,
        scope: CoroutineScope,
        networkConnected: Boolean
    ): SourceResult<PhotoData> {
        return if (networkConnected) getRemotePhotoData(query, scope) else getLocalPhotoData()
    }

    private fun getRemotePhotoData(query: String?, scope: CoroutineScope): SourceResult<PhotoData> {
        val factory = PhotoPageDataSourceFactory(query, localDataSource, remoteDataSource, scope)

        val pageList =
            LivePagedListBuilder(factory, pagedListConfig()).build()

        val networkState = Transformations.switchMap(factory.liveData) {
            it.networkState
        }

        return SourceResult(pageList, networkState)
    }

    private fun getLocalPhotoData(): SourceResult<PhotoData> {
        val dataSource =
            localDataSource.getData().map {
                PhotoData(0, 0, 0, mutableListOf(it.toPhoto()))
            }

        val pageList =
            LivePagedListBuilder(dataSource, pagedListConfig()).build()

        return SourceResult(pageList)
    }
}