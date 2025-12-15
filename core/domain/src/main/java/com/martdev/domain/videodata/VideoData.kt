package com.martdev.domain.videodata

data class VideoPost(
    val videoItems: List<VideoData>,
    val nextOffset: Int = 0
)
data class VideoData(
    val id: Long = 0,
    val videoImage: String = "",
    val videoUrl: String = "",
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