package com.martdev.android.data.repo

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.martdev.android.data.Repository
import com.martdev.android.data.SourceResult
import com.martdev.android.data.pagedListConfig
import com.martdev.android.data.pagingfactory.VideoPageDataSourceFactory
import com.martdev.android.data.toVideo
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope

class VideoDataRepo(
    private val remoteDataSource: RemoteDataSource<VideoData>,
    private val localDataSource: LocalDataSource<VideoDataEntity>
) : Repository<Video> {

    override fun getData(
        query: String?,
        scope: CoroutineScope,
        networkConnected: Boolean
    ): SourceResult<Video> {
        return if (networkConnected) getRemoteVideoData(query, scope) else getLocalVideoData()
    }

    private fun getRemoteVideoData(query: String?, scope: CoroutineScope): SourceResult<Video> {

        val factory = VideoPageDataSourceFactory(query, localDataSource, remoteDataSource, scope)

        val pageList =
            LivePagedListBuilder(factory, pagedListConfig()).build()

        val networkState = Transformations.switchMap(factory.liveData) {
            it.networkState
        }

        val retryCallback = factory.liveData.value?.retryCallback
        return SourceResult(pageList, networkState, retryCallback)
    }

    private fun getLocalVideoData(): SourceResult<Video> {
        val dataSource =
            localDataSource.getData().map {
            it.toVideo()
        }

        val pageList =
            LivePagedListBuilder(dataSource, pagedListConfig()).build()

        return SourceResult(pageList)
    }
}