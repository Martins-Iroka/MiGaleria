package com.martdev.android.local

import androidx.paging.DataSource
import com.martdev.android.local.dao.PhotoDataDao
import com.martdev.android.local.entity.PhotoEntity

class PhotoDataSource(private val photoData: PhotoDataDao) : LocalDataSource<PhotoEntity> {
    override fun getData(): DataSource.Factory<Int, PhotoEntity> {
        return photoData.getPhotoData()
    }

    override suspend fun saveData(data: PhotoEntity) {
        photoData.savePhotoData(data)
    }

    override suspend fun deleteData() {
        photoData.deletePhotoData()
    }
}