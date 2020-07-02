package com.martdev.android.data

import androidx.paging.PagedList
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoSrc
import com.martdev.android.domain.videomodel.User
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoFile
import com.martdev.android.domain.videomodel.VideoPicture
import com.martdev.android.local.entity.*

private const val PAGE_SIZE = 15

fun pagedListConfig() = PagedList.Config.Builder()
    .setInitialLoadSizeHint(PAGE_SIZE)
    .setPageSize(PAGE_SIZE)
    .build()

/**
 * Extension function that maps [Photo] to [PhotoEntity] for local storage
 */
fun Photo.toEntity() = PhotoEntity(
    id, width, height, url, photographer, photographer_url,
    photographer_id, src.toSrcEntity(), liked
)

private fun PhotoSrc.toSrcEntity() = PhotoSrcEntity(
    original, large2x, large, medium, small, portrait, landscape, tiny
)

/**
 * Extension function that maps [Video] to [VideoDataEntity] for local storage
 */
fun Video.toEntity() = VideoDataEntity(
    full_res, tags, id, width, height, url, image, duration, user.toUserEntity(),
    video_files.toVideoFilesEntity(), video_pictures.toVideoPictureEntity()
)

private fun User.toUserEntity() = UserEntity(id, name, url)

private fun List<VideoFile>.toVideoFilesEntity(): List<VideoFileEntity> {
    val files = mutableListOf<VideoFileEntity>()

    this.forEach {
        val videoFileEntity = VideoFileEntity(
            it.id, it.quality, it.file_type, it.width, it.height,
            it.link
        )
        files.add(videoFileEntity)
    }

    return files
}

private fun List<VideoPicture>.toVideoPictureEntity(): List<VideoPictureEntity> {
    val pictures = mutableListOf<VideoPictureEntity>()

    this.forEach {
        val videoPictureEntity = VideoPictureEntity(it.id, it.picture, it.nr)
        pictures.add(videoPictureEntity)
    }

    return pictures
}

/**
 * Extension function that maps [PhotoEntity] from local storage to [Photo]
 */

fun PhotoEntity.toPhoto() = Photo(
    id, width, height, url, photographer, photographer_url, photographer_id, src.toPhotoSrc(), liked
)

private fun PhotoSrcEntity.toPhotoSrc() = PhotoSrc(
    original, large2x, large, medium, small, portrait, landscape, tiny
)

/**
 * Extension function that maps [VideoDataEntity] from local storage to [Video]
 */

fun VideoDataEntity.toVideo() = Video(
    null, emptyList(), id, width, height, url, image, duration, user.toUser(),
    video_files.toVideoFiles(), video_pictures.toVideoPicture()
)

private fun UserEntity.toUser() = User(id, name, url)

private fun List<VideoFileEntity>.toVideoFiles(): List<VideoFile> {
    val files = mutableListOf<VideoFile>()

    this.forEach {
        val videoFile = VideoFile(
            it.id, it.quality, it.file_type, it.width, it.height,
            it.link
        )
        files.add(videoFile)
    }

    return files
}

private fun List<VideoPictureEntity>.toVideoPicture(): List<VideoPicture> {
    val pictures = mutableListOf<VideoPicture>()

    this.forEach {
        val videoPicture = VideoPicture(it.id, it.picture, it.nr)
        pictures.add(videoPicture)
    }

    return pictures
}