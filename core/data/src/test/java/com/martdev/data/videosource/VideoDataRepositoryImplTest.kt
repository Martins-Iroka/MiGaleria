@file:Suppress("UnusedFlow")

package com.martdev.data.videosource

import com.martdev.data.util.toVideoDataInfo
import com.martdev.data.util.toVideoEntity
import com.martdev.data.util.toVideoFileEntity
import com.martdev.data.util.toVideoImageUrlAndIdData
import com.martdev.domain.videodata.VideoData
import com.martdev.domain.videodata.VideoDataSource
import com.martdev.domain.videodata.VideoFileData
import com.martdev.domain.videodata.VideoImageUrlAndIdData
import com.martdev.local.entity.VideoEntity
import com.martdev.local.entity.VideoFileEntity
import com.martdev.local.entity.VideoImageUrlAndID
import com.martdev.local.videodatasource.VideoLocalDataSource
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.video.model.VideoFilesResponse
import com.martdev.remote.video.model.VideoPostResponse
import com.martdev.remote.video.model.VideoPostResponsePayload
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class VideoDataRepositoryImplTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var localSource: VideoLocalDataSource

    @MockK
    private lateinit var remoteSource: RemoteDataSource<VideoPostResponsePayload>

    private lateinit var videoDataRepository: VideoDataSource

    @Before
    fun setUp() {
        clearAllMocks()
        videoDataRepository = VideoDataRepositoryImpl(
            localSource, remoteSource
        )
    }

    @Test
    fun testGetVideoDataByIdFromLocalDB_confirmResultIsVideoDataInfo() = runTest {
        val videoEntity = VideoEntity(
            1, "", "", 5
        )
        val videoFiles = listOf(
            VideoFileEntity(
                videoId = 1, videoSize = 5, videoLink = ""
            )
        )

        val videoData = VideoData(
            1, "", 5, false, videoFiles.map { VideoFileData(it.videoLink, it.videoSize) }
        )
        val map = mapOf(
            Pair(videoEntity, videoFiles)
        )

        mockkStatic(Map<VideoEntity, List<VideoFileEntity>>::toVideoDataInfo)

        val id = slot<Long>()

        every { map.toVideoDataInfo() } returns videoData

        every { localSource.getVideoEntityByID(capture(id)) } answers {
            assertEquals(1, id.captured)
            flowOf(map)
        }

        val videoDataInfoResult = videoDataRepository.getVideoDataById(1).first()

        verifyOrder {
            localSource.getVideoEntityByID(1)
            map.toVideoDataInfo()
        }

        Assert.assertEquals(videoData, videoDataInfoResult)
    }

    @Test
    fun getVideoImageUrlAndIdFromDB_confirmResultIsVideoImageUrlAndIdData() = runTest {
        val videoImageUrlAndIds = listOf(
            VideoImageUrlAndID(
                1, "image1"
            ),
            VideoImageUrlAndID(
                2, "image2"
            )
        )

        val videoImageUrlAndIdDataList = listOf(
            VideoImageUrlAndIdData(
                1, "image1"
            ),
            VideoImageUrlAndIdData(
                2, "image2"
            )
        )

        mockkStatic(List<VideoImageUrlAndID>::toVideoImageUrlAndIdData)

        every { videoImageUrlAndIds.toVideoImageUrlAndIdData() } returns videoImageUrlAndIdDataList

        every { localSource.getVideoImageURLAndID() } returns flowOf(videoImageUrlAndIds)

        val videoResult = videoDataRepository.getVideoImageUrlAndId().first()

        verifyOrder {
            localSource.getVideoImageURLAndID()
            videoImageUrlAndIds.toVideoImageUrlAndIdData()
        }

        Assert.assertEquals(videoImageUrlAndIdDataList, videoResult)
    }

    @Test
    fun testRefreshVideos_verifyRemoteLoadIsCalled() = runTest {
        val videoDataAPI = VideoPostResponsePayload(
            data = listOf(
                VideoPostResponse(
                    id = 1,
                    videoFiles = listOf(
                        VideoFilesResponse(),
                        VideoFilesResponse()
                    )
                ),
                VideoPostResponse(
                    id = 2,
                    videoFiles = listOf(
                        VideoFilesResponse(),
                        VideoFilesResponse()
                    )
                )
            )
        )

        val videoEntities = listOf(
            VideoEntity(1, "", "", 5),
            VideoEntity(2, "", "", 5)
        )

        val videoFiles = listOf(
            VideoFileEntity(videoId = 1, videoLink = "", videoSize = 123),
            VideoFileEntity(videoId = 1, videoLink = "", videoSize = 456),
            VideoFileEntity(videoId = 2, videoLink = "", videoSize = 234),
            VideoFileEntity(videoId = 2, videoLink = "", videoSize = 567)
        )

        mockkStatic(List<VideoPostResponse>::toVideoEntity)
        mockkStatic(List<VideoPostResponse>::toVideoFileEntity)

        coEvery { localSource.deleteVideoEntity() } returns 1

        every { remoteSource.load() } returns flowOf(videoDataAPI)

        every { videoDataAPI.data.toVideoEntity() } returns videoEntities

        every { videoDataAPI.data.toVideoFileEntity() } returns videoFiles

        coEvery { localSource.saveVideoEntity(any()) } returns listOf(2)

        coEvery { localSource.saveVideoFiles(any()) } returns listOf(4)

        videoDataRepository.refreshVideos()

        coVerifyOrder {
            localSource.deleteVideoEntity()
            remoteSource.load()
            videoDataAPI.data.toVideoEntity()
            videoDataAPI.data.toVideoFileEntity()
            localSource.saveVideoEntity(any())
            localSource.saveVideoFiles(any())
        }
    }

    @Test
    fun testUpdateBookmarkStatus_confirmBookmarkStatusUpdate() = runTest {

        coEvery { localSource.updateBookmarkStatus(any(), any()) } returns 1

        val updatedBookmarkStatusRowCount = videoDataRepository.updateBookmarkStatus(1, true)

        Assert.assertTrue(updatedBookmarkStatusRowCount == 1)
    }
}