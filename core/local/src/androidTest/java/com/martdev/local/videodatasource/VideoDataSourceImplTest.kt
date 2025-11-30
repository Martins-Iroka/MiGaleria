package com.martdev.local.videodatasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.martdev.local.database.MyGalleryDB
import com.martdev.local.videoEntities
import com.martdev.local.videoFileEntities
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class VideoDataSourceImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var videoDataSource: VideoLocalDataSource

    private lateinit var database: MyGalleryDB

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyGalleryDB::class.java
        ).build()

        videoDataSource = VideoLocalDataSourceImpl(database.videoDataDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveVideoEntity_getVideoEntity_deleteVideoEntity_confirmDeletion() = runTest {

        videoDataSource.saveVideoEntity(videoEntities)
        videoDataSource.saveVideoFiles(videoFileEntities)

        val videoImageUrlAndID = videoDataSource.getVideoImageURLAndID().first()

        Assert.assertTrue(videoImageUrlAndID.isNotEmpty())

        videoDataSource.deleteVideoEntity()

        val videoImageUrlAndID2 = videoDataSource.getVideoImageURLAndID().first()

        Assert.assertTrue(videoImageUrlAndID2.isEmpty())
    }

    @Test
    fun saveVideoEntity_getMapOfVideoEntityAndFilesWithID1_confirmResult() = runTest {

        videoDataSource.saveVideoEntity(videoEntities)
        videoDataSource.saveVideoFiles(videoFileEntities)

        val m = videoDataSource.getVideoEntityByID(1).first()

        Assert.assertEquals(1, m.keys.first().id)
        Assert.assertEquals("image1", m.keys.first().image)
        Assert.assertTrue(m.values.isNotEmpty())
        Assert.assertTrue(m.values.first().isNotEmpty())
        Assert.assertEquals("See ${m.values.first()}", 2, m.values.first().size)
        Assert.assertEquals(1, m.values.first().first().videoId)
    }

    @Test
    fun saveVideoEntity_getMapOfVideoEntityAndFilesWithID2_confirmResult() = runTest {

        videoDataSource.saveVideoEntity(videoEntities)
        videoDataSource.saveVideoFiles(videoFileEntities)

        val m = videoDataSource.getVideoEntityByID(2).first()

        Assert.assertEquals(2, m.keys.last().id)
        Assert.assertEquals("image2", m.keys.last().image)
        Assert.assertTrue(m.values.isNotEmpty())
        Assert.assertTrue(m.values.first().isNotEmpty())
        Assert.assertEquals(2, m.values.first().size)
        Assert.assertEquals(2, m.values.first().first().videoId)
    }

    @Test
    fun saveVideoEntity_getVideoImageURLAndID_confirmResult() = runTest {

        videoDataSource.saveVideoEntity(videoEntities)
        videoDataSource.saveVideoFiles(videoFileEntities)

        val videoImageUrlAndID = videoDataSource.getVideoImageURLAndID().first()

        Assert.assertTrue(videoImageUrlAndID.isNotEmpty())
        Assert.assertEquals(2, videoImageUrlAndID.size)
        Assert.assertEquals(1, videoImageUrlAndID.first().id)
        Assert.assertEquals("image1", videoImageUrlAndID.first().image)
        Assert.assertEquals(2, videoImageUrlAndID.last().id)
        Assert.assertEquals("image2", videoImageUrlAndID.last().image)
    }

    @Test
    fun saveVideoEntity_VideoFilesEntity() = runTest {

        val rows = videoDataSource.saveVideoEntity(videoEntities)
        val rows2 = videoDataSource.saveVideoFiles(videoFileEntities)

        Assert.assertTrue(rows.isNotEmpty())
        Assert.assertEquals(2, rows.size)

        Assert.assertTrue(rows2.isNotEmpty())
        Assert.assertEquals(4, rows2.size)
    }

    @Test
    fun saveVideoEntity_VideoFilesEntity_updateBookmarkStatus_confirmUpdate() = runTest {

        videoDataSource.saveVideoEntity(videoEntities)
        videoDataSource.saveVideoFiles(videoFileEntities)

        val updatedRow = videoDataSource.updateBookmarkStatus(1, true)

        Assert.assertEquals(1, updatedRow)

        val videoEntity = videoDataSource.getVideoEntityByID(1).first()

        Assert.assertTrue(videoEntity.keys.first().bookmarked)
    }
}