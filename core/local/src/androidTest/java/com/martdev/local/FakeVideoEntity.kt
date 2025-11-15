package com.martdev.local

import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity

val videoEntities = listOf(
    VideoEntity(
        id = 1,
        url = "ik_link1",
        image = "image1",
        duration = 5
    ),
    VideoEntity(
        id = 2,
        url = "ik_link2",
        image = "image2",
        duration = 10
    )
)
val videoFileEntities = listOf(
    VideoFileEntity(
        videoId = 1,
        quality = "sd",
        videoLink = "sd_link",
        videoSize = 1
    ),
    VideoFileEntity(
        videoId = 1,
        quality = "hd",
        videoLink = "hd_link",
        videoSize = 2
    ),
    VideoFileEntity(
        videoId = 2,
        quality = "sd",
        videoLink = "sd_link",
        videoSize = 1
    ),
    VideoFileEntity(
        videoId = 2,
        quality = "hd",
        videoLink = "hd_link",
        videoSize = 2
    )
)