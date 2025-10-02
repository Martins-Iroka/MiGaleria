package com.martdev.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

data class VideoDataEntity(
    @Embedded val videoEntity: VideoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "videoId"
    ) val videoFiles: List<VideoFileEntity>
)

@Entity(
    tableName = "video_data"
)
data class VideoEntity(
    @PrimaryKey val id: Long,
    val url: String,
    val image: String,
    val duration: Int,
    val bookmarked: Boolean = false
)

@Entity(
    tableName = "video_file",
    foreignKeys = [ForeignKey(
        entity = VideoEntity::class,
        parentColumns = ["id"],
        childColumns = ["videoId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )])
data class VideoFileEntity(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val videoId: Long,
    val quality: String,
    val link: String,
    val size: Long
)

data class VideoImageUrlAndID(
    val id: Long,
    val image: String
)