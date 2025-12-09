package com.martdev.domain.photodata

data class CreatePhotoCommentData(
    val userID: Long,
    val content: String
)

data class PhotoPostComments(
    val content: String,
    val createdAt: String,
    val username: String,
    val id: Long
)