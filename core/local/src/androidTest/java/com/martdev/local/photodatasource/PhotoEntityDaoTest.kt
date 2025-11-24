package com.martdev.local.photodatasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.martdev.local.database.MyGalleryDB
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.photoEntity
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
class PhotoEntityDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MyGalleryDB

    @Before
    fun initDB() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyGalleryDB::class.java
        ).build()
    }

    @After
    fun closeDB() = database.close()


    @Test
    fun savePhotoEntity_deletePhotoEntity_confirmDeletion() = runTest {

        database.photoDataDao().savePhotoEntity(photoEntity)

        val photoUrlAndID = database.photoDataDao().getPhotoURLAndID().first()

        Assert.assertTrue(photoUrlAndID.isNotEmpty())

        val deletedRow = database.photoDataDao().deletePhotoEntity()

        Assert.assertEquals(2, deletedRow)
    }

    @Test
    fun savePhotoEntity_getPhotoEntityById() = runTest {

        database.photoDataDao().savePhotoEntity(photoEntity)

        val photoEntity = database.photoDataDao().getPhotoEntityById(1).first()

        Assert.assertEquals(1, photoEntity.photoId)
        Assert.assertEquals("Ik", photoEntity.photographer)
    }

    @Test
    fun savePhotoEntity_getPhotoURLAndID_confirmPhotoURLAndIDData() = runTest {

        database.photoDataDao().savePhotoEntity(photoEntity)

        val photoUrlAndID = database.photoDataDao().getPhotoURLAndID().first()

        Assert.assertTrue(photoUrlAndID.isNotEmpty())
        Assert.assertEquals(2, photoUrlAndID.size)
        Assert.assertEquals(1, photoUrlAndID.component1().photoId)
        Assert.assertEquals("ik_original", photoUrlAndID.component1().original)
        Assert.assertEquals(2, photoUrlAndID.component2().photoId)
        Assert.assertEquals("mi_original", photoUrlAndID.component2().original)
    }

    @Test
    fun savePhotoEntity_confirmPhotoEntitySaved() = runTest {

        database.photoDataDao().savePhotoEntity(photoEntity)

        val photoUrlAndID = database.photoDataDao().getPhotoURLAndID().first()

        Assert.assertTrue(photoUrlAndID.isNotEmpty())
    }

    @Test
    fun savePhotoEntity_updateBookmarkStatus_confirmUpdate() = runTest {

        val photoEntity = listOf(
            PhotoEntity(
                photoId = 1,
                photographer = "Ik",
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

        database.photoDataDao().savePhotoEntity(photoEntity)

        val photoUrlAndID = database.photoDataDao().getPhotoURLAndID().first()

        Assert.assertTrue(photoUrlAndID.isNotEmpty())

        val updatedRow = database.photoDataDao().updateBookmarkStatus(2, true)

        Assert.assertEquals(1, updatedRow)
    }
}