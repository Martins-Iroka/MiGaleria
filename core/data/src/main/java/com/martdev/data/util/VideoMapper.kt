package com.martdev.data.util

import com.martdev.domain.VideoDataInfo
import com.martdev.domain.VideoFileData
import com.martdev.domain.VideoImageUrlAndIdData
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
import com.martdev.local.entity.VideoImageUrlAndID
import com.martdev.remote.remotevideo.VideoAPI

fun Map<VideoEntity, List<VideoFileEntity>>.toVideoDataInfo(): VideoDataInfo {
    require(size == 1) { "Input map must contain exactly one entry."}

    val (videoEntity, videoFiles) = entries.first()
    return VideoDataInfo(
        videoEntity.id, videoEntity.url, videoEntity.duration, videoEntity.bookmarked,
        videoFiles.map {
            VideoFileData(it.quality, it.link, it.size)
        }
    )
}

fun List<VideoAPI>.toVideoEntity() = map {
    VideoEntity(it.id, it.url, it.image, it.duration)
}

fun List<VideoAPI>.toVideoFileEntity() = map { videoAPI ->
    videoAPI.video_files.map {
        VideoFileEntity(
            videoId = videoAPI.id,
            quality = it.quality,
            size = it.size,
            link = it.link
        )
    }
}.flatten()

fun List<VideoImageUrlAndID>.toVideoImageUrlAndIdData() = map {(videoId, videoUrl) ->
    VideoImageUrlAndIdData(videoId, videoUrl)
}
