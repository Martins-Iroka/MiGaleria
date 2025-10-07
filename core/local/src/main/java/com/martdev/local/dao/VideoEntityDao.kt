package com.martdev.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
import com.martdev.local.entity.VideoImageUrlAndID
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoEntityDao {

    @Query("DELETE FROM video_data")
    suspend fun deleteVideoEntity(): Int

    @Query("SELECT * FROM video_data JOIN video_file ON videoId = id WHERE id = :id ")
    fun getVideoEntityByID(id: Long): Flow<Map<VideoEntity, List<VideoFileEntity>>>

    @Query("SELECT id, image FROM video_data")
    fun getVideoImageURLAndID(): Flow<List<VideoImageUrlAndID>>

    @Insert(onConflict = REPLACE)
    suspend fun saveVideoEntity(videoEntity: List<VideoEntity>): List<Long>

    @Insert(onConflict = REPLACE)
    suspend fun saveVideoFiles(videoFileEntity: List<VideoFileEntity>): List<Long>

    @Query("UPDATE video_data SET bookmarked = :isBookmarked WHERE id = :videoId")
    suspend fun updateBookmarkStatus(videoId: Long, isBookmarked: Boolean): Int
}