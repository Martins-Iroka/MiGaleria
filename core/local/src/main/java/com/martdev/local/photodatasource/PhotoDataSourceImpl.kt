package com.martdev.local.photodatasource

import com.martdev.local.dao.PhotoEntityDao
import com.martdev.local.doIOOperation
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.entity.PhotoUrlAndID
import kotlinx.coroutines.flow.Flow

class PhotoDataSourceImpl(
    private val photoEntityDao: PhotoEntityDao
) : PhotoDataSource {
    override suspend fun deletePhotoEntity(): Int {
        return doIOOperation { photoEntityDao.deletePhotoEntity() }
    }

    override fun getPhotoEntityById(id: Long): Flow<PhotoEntity> {
        return photoEntityDao.getPhotoEntityById(id)
    }

    override fun getPhotoURLAndID(): Flow<List<PhotoUrlAndID>> {
        return photoEntityDao.getPhotoURLAndID()
    }

    override suspend fun savePhotoEntity(photoEntity: List<PhotoEntity>): List<Long> {
       return doIOOperation { photoEntityDao.savePhotoEntity(photoEntity) }
    }

    override suspend fun updateBookmarkStatus(photoId: Long, isBookmarked: Boolean): Int {
       return doIOOperation { photoEntityDao.updateBookmarkStatus(photoId, isBookmarked) }
    }
}