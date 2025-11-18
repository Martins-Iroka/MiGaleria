package com.martdev.remote.video.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateVideoCommentRequest(
    private val userID: Long,
    private val content: String
)

@Serializable
data class CreateVideoCommentResponse(
    val created: Boolean
)

@Serializable
data class VideoPostCommentResponse(
    val content: String,
    @SerialName("created_at")
    val createdAt: String,
    val username: String
)