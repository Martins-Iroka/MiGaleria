package com.martdev.remote.photo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePhotoCommentRequest(
    private val userID: Long,
    private val content: String
)

@Serializable
data class CreatePhotoCommentResponse(
    val created: Boolean
)

@Serializable
data class PhotoPostCommentResponse(
    val content: String,
    @SerialName("created_at")
    val createdAt: String,
    val username: String
)
