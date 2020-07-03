package com.martdev.android.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martdev.android.local.dao.*
import com.martdev.android.local.entity.*

@Database(
    entities = [PhotoEntity::class, PhotoSrcEntity::class, VideoEntity::class, UserEntity::class, VideoFileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MyGalleryDB : RoomDatabase() {

    abstract fun photoData(): PhotoDataDao

    abstract fun photoSrc(): PhotoSrcDao

    abstract fun videoData(): VideoDataDao

    abstract fun userDao(): UserDao

    abstract fun videoFileDao(): VideoFileDao

    companion object {

        private var INSTANCE: MyGalleryDB? = null

        private val lock = Any()

        fun getInstance(context: Context): MyGalleryDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyGalleryDB::class.java, "MyGallery.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}