package com.martdev.domain.usecase

import com.martdev.domain.Photo
import com.martdev.domain.Repository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UnusedFlow")
class PhotoDataUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var repository: Repository<Photo>

    private lateinit var photoUseCase: PhotoDataUseCase

    @Before
    fun setUp() {
        clearMocks(repository)
        photoUseCase = PhotoDataUseCase(repository)
    }

    @Test
    fun getPhotoList_returnsListOfPhotos() = runTest {

        val r = listOf(
            Photo(
                id = 1
            ),
            Photo(
                id = 2
            ),
            Photo(
                id = 3
            )
        )

        every { repository.getData() } returns flowOf(r)

        val result = photoUseCase.invoke("").first()

        verify {
            repository.getData()
        }

        assert(result.isNotEmpty())
        assertEquals(r.first(), result.first())
    }

    @Test
    fun getPhotoList_returnsEmptyList() = runTest {

        every { repository.getData() } returns flowOf(emptyList())

        val result = photoUseCase.invoke("").first()

        verify {
            repository.getData()
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun getPhotoList_throwException() = runTest {

        every { repository.getData() } throws Exception("Error")

        assertThrows(Exception::class.java) {
            photoUseCase.invoke("")
        }
    }

}