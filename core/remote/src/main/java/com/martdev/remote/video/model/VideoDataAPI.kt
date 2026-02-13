package com.martdev.remote.video.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoPostListResponse(
    @SerialName("video_items")
    val videoItems: List<VideoPost> = emptyList(),
    @SerialName("next_page")
    val nextOffset: Int = 0
)

@Serializable
data class VideoPost(
    val id: Long,
    @SerialName("video_image")
    val videoImage: String = "",
    @SerialName("video_url")
    val videoUrl: String = "",
    val duration: Int = 0,
    @SerialName("video_files")
    val videoFiles: List<VideoFilesResponse> = emptyList()
)

@Serializable
data class VideoFilesResponse(
    @SerialName("video_link")
    val videoLink: String = "",
    @SerialName("video_size")
    val videoSize: Long = 0
)
