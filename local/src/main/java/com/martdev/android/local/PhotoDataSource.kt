package com.martdev.android.local

import androidx.paging.DataSource
import com.martdev.android.local.dao.PhotoDataDao
import com.martdev.android.local.dao.PhotoSrcDao
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.local.entity.PhotoSrcEntity

class PhotoDataSource(
    private val photoData: PhotoDataDao,
    private val photoSrcDao: PhotoSrcDao
) : LocalDataSource<PhotoEntity, PhotoDataEntity> {
    override fun getData(): DataSource.Factory<Int, PhotoDataEntity> {
        return photoData.getPhotoData()
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