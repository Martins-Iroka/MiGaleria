package com.martdev.data.util

import com.martdev.domain.PhotoDataClass
import com.martdev.local.entity.PhotoEntity
import com.martdev.remote.remotephoto.PhotoDataAPI

fun PhotoEntity.toPhotoData() = PhotoDataClass(
    photoId,
    photographer,
    photographerUrl,
    original,
    large2x,
    large,
    medium,
    small,
    portrait,
    landscape,
    tiny,
    bookmarked
)

fun PhotoDataAPI.toPhotoEntity() = this.photos.map {
    PhotoEntity(
        it.id,
        it.photographer,
        it.photographer_url,
        it.src.original,
        it.src.large2x,
        it.src.large,
        it.src.medium,
        it.src.small,
        it.src.portrait,
        it.src.landscape,
        it.src.tiny
    )
}