package com.martdev.android.local

import com.martdev.android.domain.Result
import com.martdev.android.local.dao.PhotoDataDao
import com.martdev.android.local.dao.PhotoSrcDao
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.local.entity.PhotoSrcEntity
import kotlinx.coroutines.withContext

class PhotoDataSource(
    private val photoData: PhotoDataDao,
    private val photoSrcDao: PhotoSrcDao
) : LocalDataSource<PhotoEntity, PhotoDataEntity> {
    override suspend fun getData(): Result<List<PhotoDataEntity>> {
        return try {
            Result.success(photoData.getPhotoData())
        } catch (e: Exception) {
            Result.error(e.message)
        }
    }

    override suspend fun saveData(data: PhotoEntity) {
        photoData.savePhotoData(data)
    }

    suspend fun savePhotoSrc(src: PhotoSrcEntity) {
        photoSrcDao.insert(src)
    }

    override suspend fun deleteData() {
        photoData.deletePhotoData()
    }
}