package com.martdev.data.util

import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoFileData
import com.martdev.domain.videodata.VideoImageUrlAndIdData
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
import com.martdev.local.entity.VideoImageUrlAndID
import com.martdev.remote.video.model.VideoPostResponse

fun Map<VideoEntity, List<VideoFileEntity>>.toVideoDataInfo(): VideoData {
    require(size == 1) { "Input map must contain exactly one entry."}

    val (videoEntity, videoFiles) = entries.first()
    return VideoData(
        videoEntity.id, videoEntity.url, videoEntity.duration, videoEntity.bookmarked,
        videoFiles.map {
            VideoFileData(it.videoLink, it.videoSize)
        }
    )
}

fun List<VideoPostResponse>.toVideoEntity() = map {
    VideoEntity(it.id, it.videoUrl, it.videoImage, it.duration)
}

fun List<VideoPostResponse>.toVideoFileEntity() = map { videoAPI ->
    videoAPI.videoFiles.map {
        VideoFileEntity(
            videoId = videoAPI.id,
            videoSize = it.videoSize,
            videoLink = it.videoLink
        )
    }
}.flatten()

fun List<VideoImageUrlAndID>.toVideoImageUrlAndIdData() = map {(videoId, videoUrl) ->
    VideoImageUrlAndIdData(videoId, videoUrl)
}
