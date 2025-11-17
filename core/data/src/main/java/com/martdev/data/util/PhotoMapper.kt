package com.martdev.data.util

import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoUrlAndIdData
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.entity.PhotoUrlAndID
import com.martdev.remote.photo.model.PhotoPostResponsePayload

fun PhotoEntity.toPhotoData() = PhotoData(
    photoId,
    photographer,
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

fun PhotoPostResponsePayload.toPhotoEntity() = data.map {
    PhotoEntity(
        it.id,
        it.photographer,
        it.original,
        it.large2x,
        it.large,
        it.medium,
        it.small,
        it.portrait,
        it.landscape,
        it.tiny
    )
}

fun List<PhotoUrlAndID>.toPhotoUrlAndIdData() = map { (photoId, original) ->
    PhotoUrlAndIdData(photoId, original)
}