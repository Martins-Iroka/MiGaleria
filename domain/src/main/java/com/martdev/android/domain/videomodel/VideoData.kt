package com.martdev.android.domain.videomodel

data class VideoData(
    @Transient val page: Int,
    @Transient val per_page: Int,
    @Transient val total_results: Int,
    @Transient val url: String,
    val videos: List<Video>
)

data class Video(
    @Transient val full_res: Any?,
    @Transient val tags: List<Any>,
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val image: String,
    val duration: Int,
    val user: User,
    val video_files: List<VideoFile>,
    @Transient val video_pictures: List<VideoPicture>
)

data class User(
    val id: Int,
    val name: String,
    val url: String
)

data class VideoFile(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int,
    val height: Int,
    val link: String
)

data class VideoPicture(
    val id: Int,
    val picture: String,
    val nr: Int
)