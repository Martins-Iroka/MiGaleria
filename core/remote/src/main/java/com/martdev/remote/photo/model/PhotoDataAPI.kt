package com.martdev.remote.photo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoPostResponse(
    @SerialName("photo_items")
    val photoItems: List<PhotoSrcAPI>,
    @SerialName("next_page")
    val nextPage: Int
)

@Serializable
data class PhotoSrcAPI(
    val id: Long = 0,
    val photographer: String = "",
    val original: String = "",
    val large2x: String = "",
    val large: String = "",
    val medium: String = "",
    val small: String = "",
    val portrait: String = "",
    val landscape: String = "",
    val tiny: String = ""
)
