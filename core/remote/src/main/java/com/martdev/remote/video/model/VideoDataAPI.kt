package com.martdev.remote.video.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoPostResponsePayload(
    val data: List<VideoPostResponse>
)

@Serializable
data class VideoPostResponse(
    val id: Long,
    @SerialName("video_image")
    val videoImage: String = "",
    @SerialName("video_url")
    val videoUrl: String = "",
    val duration: Int = 0,
    @SerialName("video_files")
    val videoFiles: List<VideoFilesResponse>
)

@Serializable
data class VideoFilesResponse(
    @SerialName("video_link")
    val videoLink: String = "",
    @SerialName("video_size")
    val videoSize: Long = 0
)
