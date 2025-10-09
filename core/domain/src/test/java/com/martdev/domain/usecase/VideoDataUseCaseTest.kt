package com.martdev.domain.usecase

import com.martdev.domain.VideoData
import com.martdev.domain.VideoFileData
import com.martdev.domain.VideoImageUrlAndIdData
import com.martdev.domain.videodata.VideoDataSource
import com.martdev.domain.videodata.VideoDataUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
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
class VideoDataUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var videoDataSource: VideoDataSource

    private lateinit var videoUC: VideoDataUseCase

    @Before
    fun setUp() {
        clearMocks(videoDataSource)
        videoUC = VideoDataUseCase(videoDataSource)
    }

    @Test
    fun getVideoDataById_returnVideoData_verifyCall() = runTest {


        val videoData = VideoData(
            id = 1,
            videoFiles = listOf(
                VideoFileData(),
                VideoFileData()
            )
        )

        val id = slot<Long>()

        every { videoDataSource.getVideoDataById(capture(id)) } answers {
            assertEquals(1, id.captured)
            flowOf(videoData)
        }

        val result = videoUC.getVideoDataById(1).first()

        verify {
            videoDataSource.getVideoDataById(1)
        }

        assertEquals(1, result.id)
        assertTrue(result.videoFiles.isNotEmpty())
        assertEquals(2, result.videoFiles.size)
    }

    @Test
    fun getVideoImageUrlAndId_returnList_verifyCallAndList() = runTest {

        val l = listOf(
            VideoImageUrlAndIdData(1, "image1"),
            VideoImageUrlAndIdData(2, "image2"),
        )

        every { videoDataSource.getVideoImageUrlAndId() } returns flowOf(l)

        val result = videoUC.getVideoImageUrlAndId().first()

        verify {
            videoDataSource.getVideoImageUrlAndId()
        }

        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals(1, result.component1().id)
        assertEquals(2, result.component2().id)
    }

    @Test
    fun refreshVideos_assertQueryParameterIsEmpty_verifyCall() = runTest {

        val query = slot<String>()

        coEvery { videoDataSource.refreshOrSearchVideos(capture(query)) } answers {
            assertTrue(query.captured.isEmpty())
        }

        videoUC.refreshOrSearchVideos("")

        coVerify { videoDataSource.refreshOrSearchVideos("") }
    }

    @Test
    fun refreshVideos_assertQueryParameterIsNotEmpty_equalsBatman_verifyCall() = runTest {

        val query = slot<String>()

        coEvery { videoDataSource.refreshOrSearchVideos(capture(query)) } answers {
            assertTrue(query.captured.isNotEmpty())
            assertEquals("batman", query.captured)
        }

        videoUC.refreshOrSearchVideos("batman")

        coVerify { videoDataSource.refreshOrSearchVideos("batman") }
    }

    @Test
    fun updateBookmarkStatus_verifyCall_assertRowUpdatedValue() = runTest {

        val id = slot<Long>()
        val isBookmarked = slot<Boolean>()
        coEvery { videoDataSource.updateBookmarkStatus(capture(id), capture(isBookmarked)) } answers {
            assertEquals(2, id.captured)
            assertTrue(isBookmarked.captured)
            1
        }

        val row = videoUC.updateBookmarkStatus(2, true)

        coVerify {
            videoDataSource.updateBookmarkStatus(2, true)
        }

        assertEquals(1, row)
    }
}