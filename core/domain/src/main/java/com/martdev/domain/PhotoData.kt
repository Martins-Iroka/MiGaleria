package com.martdev.domain

data class
PhotoData(
    val total_result: Int = 0,
    val page: Int = 0,
    val per_page: Int = 0,
    val photos: List<Photo> = emptyList(),
    val next_page: String = ""
)

data class Photo(
    val id: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val url: String = "",
    val photographer: String = "",
    val photographer_url: String = "",
    val photographer_id: Int = 0,
    val src: PhotoSrc = PhotoSrc(),
    val liked: Boolean = false
)

data class PhotoSrc(
    val original: String = "",
    val large2x: String = "",
    val large: String = "",
    val medium: String = "",
    val small: String = "",
    val portrait: String = "",
    val landscape: String = "",
    val tiny: String = ""
)