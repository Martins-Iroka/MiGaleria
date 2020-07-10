package com.martdev.android.data.repo

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.martdev.android.data.*
//import com.martdev.android.data.paging.VideoDataPageSource
//import com.martdev.android.data.pagingfactory.VideoPageDataSourceFactory
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.VideoDataSource
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.local.entity.VideoEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class VideoDataRepo(
    private val remoteDataSource: RemoteDataSource<VideoData>,
    private val localDataSource: LocalDataSource<VideoEntity, VideoDataEntity>
) : Repository<Video>, AnkoLogger {

    override suspend fun getData(query: String, networkConnected: Boolean): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            fetchFromRemoteOrLocal(query, networkConnected)
        }
    }

    private suspend fun fetchFromRemoteOrLocal(
        query: String,
        networkConnected: Boolean
    ): Result<List<Video>> {
        if (networkConnected) {
            val result =
                if (query.isEmpty()) remoteDataSource.load() else remoteDataSource.search(query)

            info {
               "Remote -> ${result.status} : ${result.data?.videos.toString()}"
            }
            when (result.status) {
                Result.Status.LOADING -> Result.loading(null)
                Result.Status.SUCCESS -> {
                    Log.d(VideoDataRepo::class.java.simpleName, result.data.toString())
                    val videos = result.data?.videos!!
                    refreshLocalDataSource(videos)
                    return Result.success(videos)
                }
                Result.Status.ERROR -> {
                    error {
                        result.message
                    }
                    Result.error(result.message)}
            }
        }

        val localResult = localDataSource.getData().data?.map {
            it.toVideo()
        } ?: emptyList()

        info {
            "local -> ${localResult.toString()}"
        }
        return Result.success(localResult)
    }

    private suspend fun refreshLocalDataSource(
        videos: List<Video>
    ) {
        localDataSource as VideoDataSource
        localDataSource.deleteData()
        videos.forEach { video ->
            localDataSource.run {
                saveData(video.toVideoEntity())
                saveUser(video.user.toUserEntity(video.id))
                saveVideoFile(video.video_files.toVideoFilesEntity(video.id))
            }
        }
    }

//    fun getData(
//        query: String?,
//        scope: CoroutineScope,
//        networkConnected: Boolean
//    ): SourceResult<Video> {
//        return if (networkConnected) getRemoteVideoData(query, scope) else getLocalVideoData()
//    }
//
//    private fun getRemoteVideoData(query: String?, scope: CoroutineScope): SourceResult<Video> {
//
//        val factory = VideoPageDataSourceFactory(query, localDataSource, remoteDataSource, scope)
//
//        val pageList =
//            LivePagedListBuilder(factory, pagedListConfig()).build()
//
//        val networkState = Transformations.switchMap(factory.liveData) {
//            it.networkState
//        }
//
//        val retryCallback = factory.liveData.value?.retryCallback
//        return SourceResult(pageList, networkState, retryCallback)
//    }
//
//    private fun getLocalVideoData(): SourceResult<Video> {
//        val dataSource =
//            localDataSource.getData().map {
//            it.toVideo()
//        }
//
//        val pageList =
//            LivePagedListBuilder(dataSource, pagedListConfig()).build()
//
//        return SourceResult(pageList)
//    }
}