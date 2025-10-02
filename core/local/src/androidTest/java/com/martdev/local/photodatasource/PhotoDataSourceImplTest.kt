package com.martdev.local.photodatasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.martdev.local.MyGalleryDB
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.photoEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
class PhotoDataSourceImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var photoDataSource: PhotoDataSource
    private lateinit var database: MyGalleryDB

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyGalleryDB::class.java
        ).build()

        photoDataSource = PhotoDataSourceImpl(database.photoDataDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savePhotoEntity_deletePhotoEntity_confirmDeletion() = runTest {

        photoDataSource.savePhotoEntity(photoEntity)

        val photoUrlAndID = photoDataSource.getPhotoURLAndID().first()

        assertTrue(photoUrlAndID.isNotEmpty())

        val deletedRow = photoDataSource.deletePhotoEntity()

        Assert.assertEquals(2, deletedRow)
    }

    @Test
    fun savePhotoEntity_getPhotoEntityById() = runTest {

        photoDataSource.savePhotoEntity(photoEntity)

        val photoEntity = photoDataSource.getPhotoEntityById(1).first()

        assertEquals(1, photoEntity.photoId)
        assertEquals("Ik", photoEntity.photographer)
        assertEquals("Ik_link", photoEntity.photographerUrl)
    }

    @Test
    fun savePhotoEntity_getPhotoURLAndID_confirmPhotoURLAndIDData() = runTest {

        photoDataSource.savePhotoEntity(photoEntity)

        val photoUrlAndID = photoDataSource.getPhotoURLAndID().first()

        assertTrue(photoUrlAndID.isNotEmpty())
        assertEquals(2, photoUrlAndID.size)
        assertEquals(1, photoUrlAndID.component1().photoId)
        assertEquals("ik_original", photoUrlAndID.component1().original)
        assertEquals(2, photoUrlAndID.component2().photoId)
        assertEquals("mi_original", photoUrlAndID.component2().original)
    }

    @Test
    fun savePhotoEntity_confirmPhotoEntitySaved() = runTest {

        photoDataSource.savePhotoEntity(photoEntity)

        val photoUrlAndID = photoDataSource.getPhotoURLAndID().first()

        assertTrue(photoUrlAndID.isNotEmpty())
    }

    @Test
    fun savePhotoEntity_updateBookmarkStatus_confirmUpdate() = runTest {

        val photoEntity = listOf(
            PhotoEntity(
                photoId = 1,
                photographer = "Ik",
                photographerUrl = "Ik_link",
                original = "ik_original",
                large2x = "large2x",
                large = "large",
                medium = "medium",
                small = "small",
                portrait = "portrait",
                landscape = "landscape",
                tiny = "tiny"
            ),
            PhotoEntity(
                photoId = 2,
                photographer = "MI",
                photographerUrl = "MI_link",
                original = "mi_original",
                large2x = "large2x",
                large = "large",
                medium = "medium",
                small = "small",
                portrait = "portrait",
                landscape = "landscape",
                tiny = "tiny"
            )
        )

        photoDataSource.savePhotoEntity(photoEntity)

        val photoUrlAndID = photoDataSource.getPhotoURLAndID().first()

        assertTrue(photoUrlAndID.isNotEmpty())

        photoDataSource.updateBookmarkStatus(2, true)

        val p = photoDataSource.getPhotoEntityById(2).first()

        assertTrue(p.bookmarked)
    }
}