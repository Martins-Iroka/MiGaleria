package com.martdev.data.videosource

import com.martdev.data.util.toVideoDataInfo
import com.martdev.data.util.toVideoEntity
import com.martdev.data.util.toVideoFileEntity
import com.martdev.domain.VideoDataInfo
import com.martdev.domain.VideoImageUrlAndIdData
import com.martdev.local.videodatasource.VideoLocalDataSource
import com.martdev.remote.remotevideo.VideoRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class VideoDataRepositoryImpl(
    private val localDataSource: VideoLocalDataSource,
    private val remoteDataSource: VideoRemoteDataSource
) : VideoDataRepositorySource {

    override fun getVideoEntityById(id: Long): Flow<VideoDataInfo> {
        return localDataSource.getVideoEntityByID(id).map {
            it.toVideoDataInfo()
        }
    }

    override fun getVideoImageUrlAndId(): Flow<List<VideoImageUrlAndIdData>> {
        return localDataSource.getVideoImageURLAndID().map {
            it.map {(videoId, videoUrl) ->
                VideoImageUrlAndIdData(videoId, videoUrl)
            }
        }
    }

    override suspend fun refreshOrSearchVideos(query: String) {
        localDataSource.deleteVideoEntity()
        val videoResult = if (query.isEmpty()) remoteDataSource.load().first() else remoteDataSource.search(query).first()
        val videoEntities = videoResult.videos.map { it.toVideoEntity() }
        val videoFileEntities = videoResult.videos.map { it.toVideoFileEntity() }.flatten()
        localDataSource.saveVideoEntity(videoEntities)
        localDataSource.saveVideoFiles(videoFileEntities)
    }

    override suspend fun updateBookmarkStatus(
        videoId: Long,
        isBookmarked: Boolean
    ): Int {
        return localDataSource.updateBookmarkStatus(videoId, isBookmarked)
    }
}