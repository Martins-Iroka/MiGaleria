package com.martdev.domain.photodata

data class PhotoData(
    val photoId: Long = 0,
    val photographer: String = "",
    val photographerUrl: String = "",
    val original: String = "",
    val large2x: String = "",
    val large: String = "",
    val medium: String = "",
    val small: String = "",
    val portrait: String = "",
    val landscape: String = "",
    val tiny: String = "",
    val bookmarked: Boolean = false
)

data class PhotoUrlAndIdData(
    val photoId: Long,
    val original: String
)