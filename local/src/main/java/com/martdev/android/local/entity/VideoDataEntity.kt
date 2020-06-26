package com.martdev.android.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "video_data")
data class VideoDataEntity(
    @Ignore val full_res: Any? = null,
    @Ignore val tags: List<Any> = emptyList(),
    @PrimaryKey val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val image: String,
    val duration: Int,
    val user: UserEntity,
    val video_files: List<VideoFileEntity>,
    val video_pictures: List<VideoPictureEntity>
)

data class UserEntity(
    val id: Int,
    val name: String,
    val url: String
)

data class VideoFileEntity(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int,
    val height: Int,
    val link: String
)

data class VideoPictureEntity(
    val id: Int,
    val picture: String,
    val nr: Int
)