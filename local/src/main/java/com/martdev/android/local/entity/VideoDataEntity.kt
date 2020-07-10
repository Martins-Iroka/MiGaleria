package com.martdev.android.local.entity

import androidx.room.*

data class VideoDataEntity(
    @Embedded val videoEntity: VideoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    ) val user: UserEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "videoId"
    ) val videoFiles: List<VideoFileEntity>
)

@Entity(tableName = "video_data")
data class VideoEntity(
    @PrimaryKey val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val image: String,
    val duration: Int
)

@Entity(
    tableName = "user",
    foreignKeys = [ForeignKey(
        entity = VideoEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )])
data class UserEntity(
    @PrimaryKey val userId: Long,
    val id: Int,
    val name: String,
    val url: String
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
    @PrimaryKey val videoId: Long,
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int?,
    val height: Int?,
    val link: String
)