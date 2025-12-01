package com.martdev.data.photosource

import com.martdev.common.NetworkResult
import com.martdev.data.util.toPhotoData
import com.martdev.data.util.toPhotoEntity
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.CreatePhotoCommentData
import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.local.entity.PhotoEntity
import com.martdev.local.photodatasource.PhotoLocalDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.photo.PhotoRemoteDataSource
import com.martdev.remote.photo.model.CreatePhotoCommentRequest
import com.martdev.remote.photo.model.CreatePhotoCommentResponse
import com.martdev.remote.photo.model.PhotoPostCommentResponse
import com.martdev.remote.photo.model.PhotoSrcAPI
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class PhotoDataRepositoryImplTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var remote: PhotoRemoteDataSource

    @MockK
    private lateinit var local: PhotoLocalDataSource

    private lateinit var source: PhotoDataSource

    @Before
    fun setup() {
        source = PhotoDataRepositoryImpl(local, remote)
    }

    @Test
    fun `get photo from local storage`() = runTest {
        mockkStatic(PhotoEntity::toPhotoData)

        val idSlot = slot<Long>()
        every {
            local.getPhotoEntityById(capture(idSlot))
        } answers {
            assertEquals(1, idSlot.captured)
            flowOf(PhotoEntity(
                photoId = 1,
                original = "original"
            ))
        }

        val r = source.getPhotoDataById(1).first()

        assertEquals(1, r.photoId)
        assertEquals("original", r.original)

        verify {
            local.getPhotoEntityById(any())
        }
    }

    @Test
    fun `get remote photos save it then return list`() = runTest {
        mockkStatic(List<PhotoSrcAPI>::toPhotoEntity)
        mockkStatic(List<PhotoSrcAPI>::toPhotoData)

        every {
            remote.getAllPhotoPosts(any(), any())
        } returns flowOf(
            NetworkResult.Success(
                ResponseDataPayload(
                    listOf(
                        PhotoSrcAPI(
                            id = 1,
                            original = "original"
                        ),
                        PhotoSrcAPI(
                            id = 2,
                            original = "original2"
                        )
                    )
                )
            )
        )

        coEvery { local.savePhotoEntity(any()) } returns listOf(2)

        val r = source.getPhotos(1, 1).first()

        assertTrue(r is ResponseData.Success)
        assertTrue(r.data.isNullOrEmpty().not())

        coVerify {
            local.savePhotoEntity(any())
        }
    }

    @Test
    fun `get remote photos response failed`() = runTest {

        every {
            remote.getAllPhotoPosts(any(), any())
        } returns flowOf(
            NetworkResult.Failure.InternalServerError()
        )

        val r = source.getPhotos(1, 1).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)
    }

    @Test
    fun `post comment confirm it was successful`() = runTest {

        val postIdSlot = slot<String>()
        val commentSlot = slot<CreatePhotoCommentRequest>()
        val createCommentRequest = CreatePhotoCommentRequest(
            userID = 1,
            content = "test"
        )
        every {
            remote.postComment(capture(postIdSlot), capture(commentSlot))
        } answers {
            assertEquals("1", postIdSlot.captured)
            assertEquals(createCommentRequest, commentSlot.captured)
            flowOf(NetworkResult.Success(ResponseDataPayload(CreatePhotoCommentResponse(true))))
        }

        val r = source.postComment("1", CreatePhotoCommentData(1, "test")).first()

        assertTrue(r is ResponseData.Success)

        verify {
            remote.postComment(any(), any())
        }
    }

    @Test
    fun `post comment but response failed due to internal server error`() = runTest {

        val postIdSlot = slot<String>()
        val commentSlot = slot<CreatePhotoCommentRequest>()
        val createCommentRequest = CreatePhotoCommentRequest(
            userID = 1,
            content = "test"
        )
        every {
            remote.postComment(capture(postIdSlot), capture(commentSlot))
        } answers {
            assertEquals("1", postIdSlot.captured)
            assertEquals(createCommentRequest, commentSlot.captured)
            flowOf(NetworkResult.Failure.InternalServerError())
        }

        val r = source.postComment("1", CreatePhotoCommentData(1, "test")).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)

        verify {
            remote.postComment(any(), any())
        }
    }

    @Test
    fun `get comments by post id confirm response is successful`() = runTest {
        val postIDSlot = slot<String>()

        every {
            remote.getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            flowOf(
                NetworkResult.Success(
                    ResponseDataPayload(
                        listOf(
                            PhotoPostCommentResponse(
                                content = "content1",
                                createdAt = "2025-12-01",
                                username = "martdev"
                            ),
                            PhotoPostCommentResponse(
                                content = "content2",
                                createdAt = "2025-12-02",
                                username = "martdev"
                            )
                        )
                    )
                )
            )
        }

        val r = source.getCommentsByPostID("1").first()

        assertTrue(r is ResponseData.Success)
        assertTrue(r.data.isNullOrEmpty().not())
        assertEquals("content2", r.data!!.last().content)
    }

    @Test
    fun `get comments by post id confirm response failed`() = runTest {
        val postIDSlot = slot<String>()

        every {
            remote.getCommentsByPostID(capture(postIDSlot))
        } answers {
            assertEquals("1", postIDSlot.captured)
            flowOf(
                NetworkResult.Failure.InternalServerError()
            )
        }

        val r = source.getCommentsByPostID("1").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)
    }
}