package com.martdev.android.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martdev.android.local.dao.PhotoDataDao
import com.martdev.android.local.dao.VideoDataDao
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.local.entity.VideoDataEntity

@Database(entities = [PhotoEntity::class, VideoDataEntity::class], version = 1, exportSchema = false)
abstract class MyGalleryDB : RoomDatabase(){

    abstract fun photoData(): PhotoDataDao

    abstract fun videoData(): VideoDataDao

    companion object {

        private var INSTANCE: MyGalleryDB? = null

        private val lock = Any()

        fun getInstance(context: Context): MyGalleryDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    MyGalleryDB::class.java, "MyGallery.db")
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}