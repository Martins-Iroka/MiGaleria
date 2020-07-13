package com.martdev.android.data.repo

import com.martdev.android.data.*
import com.martdev.android.domain.Repository
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.VideoDataSource
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.local.entity.VideoEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoDataRepo(
    private val remoteDataSource: RemoteDataSource<VideoData>,
    private val localDataSource: LocalDataSource<VideoEntity, VideoDataEntity>
) : Repository<Video> {

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

            when (result.status) {
                Result.Status.LOADING -> Result.loading(null)
                Result.Status.SUCCESS -> {
                    val videos = result.data?.videos!!
                    refreshLocalDataSource(videos)
                    return Result.success(videos)
                }
                Result.Status.ERROR -> Result.error(result.message)
            }
        }

        val localResult = localDataSource.getData().data?.map {
            it.toVideo()
        } ?: emptyList()

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
}