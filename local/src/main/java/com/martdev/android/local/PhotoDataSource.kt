package com.martdev.android.local

import androidx.paging.DataSource
import com.martdev.android.local.dao.PhotoDataDao
import com.martdev.android.local.entity.PhotoDataEntity

class PhotoDataSource(private val photoData: PhotoDataDao) : LocalDataSource<PhotoDataEntity> {
    override fun getData(): DataSource.Factory<Int, PhotoDataEntity> {
        return photoData.getPhotoData()
    }

    override suspend fun saveData(data: PhotoDataEntity) {
        photoData.savePhotoData(data)
    }

    override suspend fun deleteData() {
        photoData.deletePhotoData()
    }
}