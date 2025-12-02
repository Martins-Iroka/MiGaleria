package com.martdev.photo

import app.cash.turbine.test
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoData
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PhotoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: PhotoDataUseCase

    private lateinit var viewmodel: PhotoViewModel

    @Before
    fun setUp() {
        viewmodel = PhotoViewModel(useCase)
    }

    @Test
    fun `get photos should emit loading then success when use case returns success`() = runTest {
        val limitSlot = slot<Int>()
        val offset = slot<Int>()

        every {
            useCase.getPhotos(capture(limitSlot), capture(offset))
        } answers {
            assertEquals(2, limitSlot.captured)
            assertEquals(1, offset.captured)
            flowOf(
                ResponseData.Success(
                    listOf(
                        PhotoData(
                            photoId = 1
                        ),
                        PhotoData(
                            photoId = 2
                        )
                    )
                )
            )
        }

        viewmodel.photos.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.getPhotos(2, 1)

            assertEquals(ResponseData.Loading, awaitItem())

            assertIs<ResponseData.Success<*>>(awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun `get photos should emit loading then fail when use case returns error`() = runTest {
        val limitSlot = slot<Int>()
        val offset = slot<Int>()

        every {
            useCase.getPhotos(capture(limitSlot), capture(offset))
        } answers {
            assertEquals(2, limitSlot.captured)
            assertEquals(1, offset.captured)
            flowOf(
                ResponseData.Error("error")
            )
        }

        viewmodel.photos.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.getPhotos(2, 1)

            assertEquals(ResponseData.Loading, awaitItem())

            val r = awaitItem()
            assertIs<ResponseData.Error>(r)
            assertEquals("error", r.message)

            expectNoEvents()
        }
    }

    @Test
    fun `get photos should emit loading then use case throws an exception`() = runTest {
        val limitSlot = slot<Int>()
        val offset = slot<Int>()

        every {
            useCase.getPhotos(capture(limitSlot), capture(offset))
        } returns flow {
            throw Exception("test error")
        }

        viewmodel.photos.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.getPhotos(2, 1)

            assertEquals(ResponseData.Loading, awaitItem())

            val r = awaitItem()
            assertIs<ResponseData.Error>(r)
            assertEquals("test error", r.message)

            cancelAndIgnoreRemainingEvents()
        }
    }
}