package com.martdev.domain.usecase

import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoUrlAndIdData
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UnusedFlow")
class PhotoDataUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var photoDataSource: PhotoDataSource

    private lateinit var photoDataUseCase: PhotoDataUseCase

    @Before
    fun setUp() {
        clearMocks(photoDataSource)
        photoDataUseCase = PhotoDataUseCase(photoDataSource)
    }

    @Test
    fun getPhotoDataById_returnPhotoData_verifyCall() = runTest {

        val id = slot<Long>()
        val photoData = PhotoData(
            photoId = 1
        )

        every { photoDataSource.getPhotoDataById(capture(id))} answers {
            assertEquals(1, id.captured)
            flowOf(photoData)
        }

        val result = photoDataUseCase.getPhotoDataById(1).first()

        verify {
            photoDataSource.getPhotoDataById(1)
        }
        assertEquals(photoData, result)
    }

    @Test
    fun loadPhotos_returnListPhotoUrlAndIdData() = runTest {

        val l = listOf(
            PhotoUrlAndIdData(
                1, "original1"
            ),
            PhotoUrlAndIdData(
                2, "original2"
            )
        )
        every { photoDataSource.loadLocalPhotos() } returns flowOf(l)

        val result = photoDataUseCase.loadPhotos().first()

        verify {
            photoDataUseCase.loadPhotos()
        }

        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals(1, l.first().photoId)
        assertEquals(2, l.last().photoId)
    }

    @Test
    fun loadPhotos_returnEmptyListOfPhotoUrlAndIdData() = runTest {

        every { photoDataSource.loadLocalPhotos() } returns flowOf(emptyList())

        val result = photoDataUseCase.loadPhotos().first()

        verify {
            photoDataUseCase.loadPhotos()
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun refreshPhotos_assertQueryParameterIsEmpty_verifyCall() = runTest {

        coEvery { photoDataSource.refreshPhotos() } just Runs

        photoDataUseCase.refreshPhotos()

        coVerify {
            photoDataSource.refreshPhotos()
        }
    }

    @Test
    fun updateBookmarkStatus_verifyCall_assertRowUpdatedValue() = runTest {

        val id = slot<Long>()
        val isBookmarked = slot<Boolean>()

        coEvery { photoDataSource.updateBookmarkStatus(capture(id), capture(isBookmarked)) } answers {
            assertEquals(1, id.captured)
            assertTrue(isBookmarked.captured)
            1
        }

        val row = photoDataUseCase.updateBookmarkStatus(1, true)

        coVerify {
            photoDataSource.updateBookmarkStatus(1, true)
        }

        assertEquals(1, row)
    }
}