package com.martdev.android.data

import androidx.paging.PagedList
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoSrc
import com.martdev.android.domain.videomodel.User
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoFile
import com.martdev.android.local.entity.*

private const val PAGE_SIZE = 15

fun pagedListConfig() = PagedList.Config.Builder()
    .setInitialLoadSizeHint(PAGE_SIZE)
    .setPageSize(PAGE_SIZE)
    .build()

/**
 * Extension function that maps [Photo] to [PhotoEntity] and [PhotoSrc] to [PhotoSrcEntity] for local storage
 */
fun Photo.toPhotoEntity() = PhotoEntity(
    id, width, height, url, photographer, photographer_url,
    photographer_id
)

fun PhotoSrc.toSrcEntity(id: Long) = PhotoSrcEntity(
    id, original, large2x, large, medium, small, portrait, landscape, tiny
)

/**
 * Extension function that maps [PhotoDataEntity] from local storage to [Photo]
 */
fun PhotoDataEntity.toPhoto(): Photo {
    return photoEntity.run {
        Photo(photoId, width, height, url, photographer, photographer_url, photographer_id,
            photoSrcEntity.toPhotoSrc())
    }
}

private fun PhotoSrcEntity.toPhotoSrc() = PhotoSrc(
    original, large2x, large, medium, small, portrait, landscape, tiny
)

/**
 * Extension function that maps [Video] to [VideoEntity] for local storage
 */
fun Video.toVideoEntity() = VideoEntity(
    id, width, height, url, image, duration
)

fun User.toUserEntity(parentId: Long) = UserEntity(parentId, id, name, url)

fun List<VideoFile>.toVideoFilesEntity(parentId: Long): List<VideoFileEntity> {
    val files = mutableListOf<VideoFileEntity>()

    this.forEach {
        val videoFileEntity = VideoFileEntity(parentId,
            it.id, it.quality, it.file_type, it.width, it.height,
            it.link
        )
        files.add(videoFileEntity)
    }

    return files
}

/**
 * Extension function that maps [VideoDataEntity] from local storage to [Video]
 */

fun VideoDataEntity.toVideo(): Video {
    return videoEntity.run {
        Video(
            id = id, width =  width, height =  height, url =  url, image =  image, duration = duration,
            user = user.toUser(), video_files = videoFiles.toVideoFiles()
        )
    }
}

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