package com.martdev.remote.remotephoto

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDataAPI(
    val total_result: Int = 0,
    val page: Int = 0,
    val per_page: Int = 0,
    val photos: List<PhotoAPI> = emptyList(),
    val next_page: String = ""
)

@Serializable
data class PhotoAPI(
    val id: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val url: String = "",
    val photographer: String = "",
    val photographer_url: String = "",
    val photographer_id: Long = 0,
    val src: PhotoSrcAPI = PhotoSrcAPI(),
    val liked: Boolean = false
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

@Serializable
data class PhotoPostResponsePayload(
    val data: List<PhotoSrcAPI>
)
