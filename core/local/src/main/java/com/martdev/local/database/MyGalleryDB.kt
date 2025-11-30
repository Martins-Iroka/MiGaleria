package com.martdev.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martdev.local.dao.PhotoEntityDao
import com.martdev.local.dao.VideoEntityDao
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity

@Database(
    entities = [PhotoEntity::class, VideoEntity::class, VideoFileEntity::class],
    version = 1, exportSchema = false
)
abstract class MyGalleryDB : RoomDatabase() {

    abstract fun photoDataDao(): PhotoEntityDao

    abstract fun videoDataDao(): VideoEntityDao

    companion object {
        private const val DATABASE_NAME = "mygallery.db"

        @Synchronized
        fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            MyGalleryDB::class.java,
            DATABASE_NAME
        ).build()
    }
}

suspend fun <T> doIOOperation(block: suspend () -> T) = block()