package com.martdev.data.videosource

import com.martdev.data.util.toVideoDataInfo
import com.martdev.data.util.toVideoEntity
import com.martdev.data.util.toVideoFileEntity
import com.martdev.data.util.toVideoImageUrlAndIdData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoImageUrlAndIdData
import com.martdev.domain.videodata.VideoDataSource
import com.martdev.local.videodatasource.VideoLocalDataSource
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.remotevideo.VideoDataAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class VideoDataRepositoryImpl(
    private val localDataSource: VideoLocalDataSource,
    private val remoteDataSource: RemoteDataSource<VideoDataAPI>
) : VideoDataSource {

    override fun getVideoDataById(id: Long): Flow<VideoData> {
        return localDataSource.getVideoEntityByID(id).map {
            it.toVideoDataInfo()
        }
    }

    override fun getVideoImageUrlAndId(): Flow<List<VideoImageUrlAndIdData>> {
        return localDataSource.getVideoImageURLAndID().map {
            it.toVideoImageUrlAndIdData()
        }
    }

    override suspend fun refreshOrSearchVideos(query: String) {
        localDataSource.deleteVideoEntity()
        val videoResult = if (query.isEmpty()) remoteDataSource.load().firstOrNull() else remoteDataSource.search(query).firstOrNull()
        videoResult?.let {
            val videoEntities = it.videos.toVideoEntity()

            val videoFileEntities = it.videos.toVideoFileEntity()
            localDataSource.saveVideoEntity(videoEntities)
            localDataSource.saveVideoFiles(videoFileEntities)
        }
    }

    override suspend fun updateBookmarkStatus(
        videoId: Long,
        isBookmarked: Boolean
    ): Int {
        return localDataSource.updateBookmarkStatus(videoId, isBookmarked)
    }
}