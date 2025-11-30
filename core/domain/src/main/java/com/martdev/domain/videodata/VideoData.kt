package com.martdev.domain.videodata

data class VideoData(
    val id: Long = 0,
    val url: String = "",
    val duration: Int = 0,
    val bookmarked: Boolean = false,
    val videoFiles: List<VideoFileData> = emptyList()
)

data class VideoFileData(
    val link: String = "",
    val size: Long = 0
)

data class VideoImageUrlAndIdData(
    val id: Long,
    val image: String
)