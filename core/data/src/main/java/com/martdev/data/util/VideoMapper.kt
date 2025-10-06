package com.martdev.data.util

import com.martdev.domain.VideoDataInfo
import com.martdev.domain.VideoFileData
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
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

fun VideoAPI.toVideoEntity() = VideoEntity(id, url, image, duration)

fun VideoAPI.toVideoFileEntity() = video_files.map {
    VideoFileEntity(
        videoId = id,
        quality = it.quality,
        size = it.size,
        link = it.link
    )
}
