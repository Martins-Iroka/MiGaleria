package com.martdev.android.local.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

data class PhotoDataEntity(
    @Embedded val photoEntity: PhotoEntity,
    @Relation(
        parentColumn = "photoId",
        entityColumn = "photoSrcId"
    )
    val photoSrcEntity: PhotoSrcEntity
)

@Entity(tableName = "photo_data")
data class PhotoEntity(
    @PrimaryKey val photoId: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Int
)

@Entity(
    tableName = "photo_src",
    foreignKeys = [ForeignKey(
        entity = PhotoEntity::class,
        parentColumns = ["photoId"],
        childColumns = ["photoSrcId"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class PhotoSrcEntity(
    @PrimaryKey val photoSrcId: Long,
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)