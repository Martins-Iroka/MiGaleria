package com.martdev.domain.usecase

import com.martdev.domain.Repository
import com.martdev.domain.Video
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
class VideoDataUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var repository: Repository<Video>

    private lateinit var videoUseCase: VideoDataUseCase

    @Before
    fun setUp() {
        clearMocks(repository)
        videoUseCase = VideoDataUseCase(repository)
    }

    @Test
    fun getVideoList_returnsListOfVideos() = runTest {

        val r = listOf(
            Video(
                id = 1
            ),
            Video(
                id = 2
            ),
            Video(
                id = 3
            ),
            Video(
                id = 4
            )
        )

        every { repository.getData() } returns flowOf(r)

        val result = videoUseCase.invoke("").first()

        verify {
            repository.getData()
        }

        assert(result.isNotEmpty())
        assertEquals(r.first(), result.first())
    }

    @Test
    fun searchVideo_returnVideoListForSearch() = runTest {
        val r = listOf(
            Video(
                id = 1,
                url = "batman"
            ),
            Video(
                id = 2,
                url = "superman"
            ),
            Video(
                id = 3,
                url = "batman"
            ),
            Video(
                id = 4,
                url = "superman"
            )
        )

        every { repository.getData(any()) } returns flowOf(r.filter { it.url == "batman" })

        val result = videoUseCase.invoke("batman").first()

        verify {
            repository.getData(any())
        }

        assert(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("batman", result.find { it.id == 3L }?.url)
    }

    @Test
    fun getVideoList_returnsEmptyList() = runTest {

        every { repository.getData() } returns flowOf(emptyList())

        val result = videoUseCase.invoke("").first()

        verify {
            repository.getData()
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun getVideoList_throwException() = runTest {

        every { repository.getData() } throws Exception("Error")

        assertThrows(Exception::class.java) {
            videoUseCase.invoke("")
        }
    }
}