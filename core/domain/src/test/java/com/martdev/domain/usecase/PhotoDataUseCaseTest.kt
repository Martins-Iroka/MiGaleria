package com.martdev.domain.usecase

import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoInfo
import com.martdev.domain.photodata.PhotoPostComments
import com.martdev.domain.photodata.PhotoUrlAndIdData
import io.mockk.Runs
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class PhotoDataUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var photoDataSource: PhotoDataSource

    private lateinit var photoDataUseCase: PhotoDataUseCase

    @Before
    fun setUp() {
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

    @Test
    fun `get all photos confirm response is successful`() = runTest {

        every {
            photoDataSource.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Success(
                PhotoInfo(
                    photoItems = listOf(
                        PhotoData(
                            photoId = 1
                        ),
                        PhotoData(
                            photoId = 1
                        )
                    ),
                    nextOffset = 1
                )
            )
        )

        val r = photoDataUseCase.getPhotoInfo(1, 1).first()

        assertTrue(r is ResponseData.Success)
        assertNotNull(r.data)
        assertTrue(r.data.photoItems.isEmpty().not())
        assertEquals(2, r.data.photoItems.size)

        verify {
            photoDataSource.getPhotoInfo(any(), any())
        }
    }

    @Test
    fun `get all photos confirm response failed`() = runTest {

        every {
            photoDataSource.getPhotoInfo(any(), any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val r = photoDataUseCase.getPhotoInfo(1, 1).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("error", r.message)

        verify {
            photoDataSource.getPhotoInfo(any(), any())
        }
    }

    @Test
    fun `post comments confirm response is successful`() = runTest {

        every {
            photoDataSource.postComment(any(), any())
        } returns flowOf(
            ResponseData.Success(null)
        )

        val r = photoDataUseCase.postComment("1", "content").first()

        assertTrue(r is ResponseData.Success)

        verify {
            photoDataSource.postComment(any(), any())
        }
    }

    @Test
    fun `post comments confirm response failed`() = runTest {

        every {
            photoDataSource.postComment(any(), any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        val r = photoDataUseCase.postComment("1", "content").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("error", r.message)

        verify {
            photoDataSource.postComment(any(), any())
        }
    }

    @Test
    fun `get comments by post id confirm response is successful`() = runTest {

        every {
            photoDataSource.getCommentsByPostID(any())
        } returns flowOf(
            ResponseData.Success(
                listOf(
                    PhotoPostComments(
                        content = "content",
                        createdAt = "2025-12-01",
                        username = "martdev",
                        id = 1
                    ),
                    PhotoPostComments(
                        content = "content2",
                        createdAt = "2025-11-30",
                        username = "martdev",
                        id = 2
                    )
                )
            )
        )

        val r = photoDataUseCase.getCommentsByPostId("1").first()

        assertTrue(r is ResponseData.Success)
        assertTrue(!(r.data.isNullOrEmpty()))
    }

    @Test
    fun `get comments by post id confirm response failed`() = runTest {

        every {
            photoDataSource.getCommentsByPostID(any())
        } returns flowOf(
            ResponseData.Error(
                "error"
            )
        )

        val r = photoDataUseCase.getCommentsByPostId("1").first()

        assertTrue(r is ResponseData.Error)
        assertEquals(
            "error",
            r.message
        )
    }
}