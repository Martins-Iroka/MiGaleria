package com.martdev.data.photosource

import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.data.util.toPhotoUrlAndIdData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.domain.photodata.PhotoUrlAndIdData
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.entity.PhotoUrlAndID
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.remotephoto.PhotoAPI
import com.martdev.remote.remotephoto.PhotoDataAPI
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class PhotoDataRepositoryImplTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var localSource: PhotoLocalDataSource

    @MockK
    private lateinit var remoteSource: RemoteDataSource<PhotoDataAPI>

    private lateinit var photoDataRepository: PhotoDataSource

    @Before
    fun setUp() {
        clearAllMocks()
        photoDataRepository = PhotoDataRepositoryImpl(
            localSource, remoteSource
        )
    }

    @Test
    fun testGetPhotoDataByIdFromLocalDB_confirmResultIsPhotoDataClass() = runTest {
        val photoEntity1 = PhotoEntity(
            photoId = 1,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        val photoData = PhotoData(1)
        mockkStatic(PhotoEntity::toPhotoData)

        val id = slot<Long>()

        every { photoEntity1.toPhotoData() } returns PhotoData(1)

        every { localSource.getPhotoEntityById(capture(id)) } answers {
            assertEquals(1, id.captured)
            flowOf(
                photoEntity1
            )
        }

        val r = photoDataRepository.getPhotoDataById(1).first()

        verifyOrder {
            localSource.getPhotoEntityById(1)
            photoEntity1.toPhotoData()
        }

        Assert.assertEquals(photoData, r)
    }

    @Test
    fun testLoadPhotosFromLocalDB_confirmResultIsPhotoUrlAndIdData() = runTest {
        val photoUrlAndIds = listOf(
            PhotoUrlAndID(
                1, "original1"
            ),
            PhotoUrlAndID(
                2, "original2"
            ),
            PhotoUrlAndID(
                3, "original3"
            )
        )

        val photoUrlAndIdDataList = listOf(
            PhotoUrlAndIdData(
                1, "original1"
            ),
            PhotoUrlAndIdData(
                2, "original2"
            ),
            PhotoUrlAndIdData(
                3, "original3"
            )
        )

        mockkStatic(List<PhotoUrlAndID>::toPhotoUrlAndIdData)

        every { photoUrlAndIds.toPhotoUrlAndIdData() } returns photoUrlAndIdDataList

        every { localSource.getPhotoURLAndID() } returns flowOf(photoUrlAndIds)

        val photoUrlAndIdData = photoDataRepository.loadPhotos().first()

        verifyOrder {
            localSource.getPhotoURLAndID()
            photoUrlAndIds.toPhotoUrlAndIdData()
        }

        Assert.assertTrue(photoUrlAndIdData.isNotEmpty())
        Assert.assertEquals(photoUrlAndIdDataList.first(), photoUrlAndIdData.first())
    }

    @Test
    fun testRefreshOrSearchPhotos_passEmptyQuerySearch_verifyRemoteLoadIsCalled() = runTest {
        val photoDataAPI = PhotoDataAPI(
            photos = listOf(
                PhotoAPI(
                    id = 1
                ),
                PhotoAPI(
                    id = 2
                )
            )
        )

        val photoEntities = listOf(
            PhotoEntity(
                photoId = 1, "", "", "", "", "",
                "",
                "",
                "",
                "",
                ""
            ),
            PhotoEntity(
                photoId = 2, "", "", "", "", "",
                "",
                "",
                "",
                "",
                ""
            )
        )

        mockkStatic(PhotoDataAPI::toPhotoEntity)

        coEvery { localSource.deletePhotoEntity() } returns 1

        coEvery { localSource.savePhotoEntity(any()) } returns listOf(3)

        every { photoDataAPI.toPhotoEntity() } returns photoEntities

        coEvery { remoteSource.load() } returns flowOf(photoDataAPI)

        photoDataRepository.refreshOrSearchPhotos("")

        coVerifyOrder {
            localSource.deletePhotoEntity()
            remoteSource.load()
            photoDataAPI.toPhotoEntity()
            localSource.savePhotoEntity(any())
        }
    }

    @Test
    fun testRefreshOrSearchPhotos_passQuerySearch_verifyRemoteSearchIsCalled() = runTest {
        val photoDataAPI = PhotoDataAPI(
            photos = listOf(
                PhotoAPI(
                    id = 1
                ),
                PhotoAPI(
                    id = 2
                )
            )
        )

        val photoEntities = listOf(
            PhotoEntity(
                photoId = 1, "", "", "", "", "",
                "",
                "",
                "",
                "",
                ""
            ),
            PhotoEntity(
                photoId = 2, "", "", "", "", "",
                "",
                "",
                "",
                "",
                ""
            )
        )

        mockkStatic(PhotoDataAPI::toPhotoEntity)

        val q = slot<String>()

        coEvery { localSource.deletePhotoEntity() } returns 1

        coEvery { localSource.savePhotoEntity(any()) } returns listOf(3)

        every { photoDataAPI.toPhotoEntity() } returns photoEntities

        coEvery { remoteSource.search(capture(q)) } answers {
            assertTrue(q.captured.isNotEmpty())
            assertEquals("batman", q.captured)
            flowOf(photoDataAPI)
        }

        photoDataRepository.refreshOrSearchPhotos("batman")

        coVerifyOrder {
            localSource.deletePhotoEntity()
            remoteSource.search(any())
            photoDataAPI.toPhotoEntity()
            localSource.savePhotoEntity(any())
        }
    }

    @Test
    fun updateBookmarkStatus_confirmBookmarkStatusUpdate() = runTest {

        coEvery { localSource.updateBookmarkStatus(any(), any()) } returns 1

        val updateBookmarkStatusRowIndex = photoDataRepository.updateBookmarkStatus(1, true)

        Assert.assertTrue(updateBookmarkStatusRowIndex == 1)
    }
}