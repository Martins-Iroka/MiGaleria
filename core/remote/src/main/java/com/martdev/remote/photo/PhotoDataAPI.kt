package com.martdev.remote.photo

import kotlinx.serialization.Serializable

@Serializable
data class PhotoPostResponsePayload(
    val data: List<PhotoSrcAPI>
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
