package com.martdev.domain

data class
PhotoData(
    val total_result: Int = 0,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val next_page: String = ""
)

data class Photo(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Int,
    val src: PhotoSrc,
    val liked: Boolean = false
)

data class PhotoSrc(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)