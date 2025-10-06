package com.martdev.remote.remotevideo

import kotlinx.serialization.Serializable

@Serializable
data class VideoDataAPI(
    val page: Int = 0,
    val per_page: Int = 0,
    val total_results: Int = 0,
    val url: String = "",
    val videos: List<VideoAPI> = emptyList()
)

@Serializable
data class VideoAPI(
    val id: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val url: String = "",
    val image: String = "",
    val duration: Int = 0,
    val user: UserAPI = UserAPI(),
    val video_files: List<VideoFileAPI> = emptyList(),
    val video_pictures: List<VideoPictureAPI> = emptyList()
)

@Serializable
data class UserAPI(
    val id: Int = 0,
    val name: String = "",
    val url: String = ""
)

@Serializable
data class VideoFileAPI(
    val id: Int = 0,
    val quality: String = "",
    val file_type: String = "",
    val width: Int? = 0,
    val height: Int? = 0,
    val link: String = "",
    val size: Long = 0
)

@Serializable
data class VideoPictureAPI(
    val id: Int = 0,
    val picture: String = "",
    val nr: Int = 0
)
