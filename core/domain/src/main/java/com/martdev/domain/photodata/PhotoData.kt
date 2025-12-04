package com.martdev.domain.photodata

import java.util.UUID

data class PhotoData(
    val photoId: Long = 0,
    val photographer: String = "",
    val original: String = "",
    val large2x: String = "",
    val large: String = "",
    val medium: String = "",
    val small: String = "",
    val portrait: String = "",
    val landscape: String = "",
    val tiny: String = "",
    val bookmarked: Boolean = false,
    val id: String = UUID.randomUUID().toString()
)

data class PhotoUrlAndIdData(
    val photoId: Long,
    val original: String
)