package com.martdev.domain

data class VideoData(
    val page: Int = 0,
    val per_page: Int = 0,
    val total_results: Int = 0,
    val url: String = "",
    val videos: List<Video> = emptyList()
)

data class Video(
    val full_res: Any? = null,
    val tags: List<Any> = emptyList(),
    val id: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val url: String = "",
    val image: String = "",
    val duration: Int = 0,
    val video_files: List<VideoFile> = emptyList(),
    val video_pictures: List<VideoPicture> = emptyList()
)

data class VideoFile(
    val id: Int = 0,
    val quality: String = "",
    val file_type: String = "",
    val width: Int? = 0,
    val height: Int? = 0,
    val link: String = ""
)

data class VideoPicture(
    val id: Int = 0,
    val picture: String = "",
    val nr: Int = 0
)

data class VideoDataInfo(
    val id: Long,
    val url: String,
    val duration: Int,
    val bookmarked: Boolean,
    val videoFiles: List<VideoFileData>
)

data class VideoFileData(
    val quality: String,
    val link: String,
    val size: Long
)

data class VideoImageUrlAndIdData(
    val id: Long,
    val image: String
)