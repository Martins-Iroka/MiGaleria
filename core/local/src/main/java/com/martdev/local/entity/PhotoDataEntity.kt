package com.martdev.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_data")
data class PhotoEntity(
    @PrimaryKey val photoId: Long,
    val photographer: String,
    val photographerUrl: String,
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String,
    val bookmarked: Boolean = false
)

data class PhotoUrlAndID(
    val photoId: Long,
    val original: String
)
