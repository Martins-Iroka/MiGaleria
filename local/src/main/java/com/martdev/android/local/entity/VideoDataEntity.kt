package com.martdev.android.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "video_data")
data class VideoDataEntity(
    @Ignore val full_res: Any?,
    @Ignore val tags: List<Any>,
    @PrimaryKey val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val image: String,
    val duration: Int,
    val user: User,
    val video_files: List<VideoFile>,
    val video_pictures: List<VideoPicture>
)

data class User(
    val id: Int,
    val name: String,
    val url: String
)

data class VideoFile(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int,
    val height: Int,
    val link: String
)

data class VideoPicture(
    val id: Int,
    val picture: String,
    val nr: Int
)