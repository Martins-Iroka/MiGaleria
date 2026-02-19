package com.martdev.photo.photodetail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoDataUseCase
import com.martdev.domain.photodata.PhotoPostComments
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class PhotoDetailViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: PhotoDataUseCase

    private lateinit var viewmodel: PhotoDetailViewModel

    @Test
    fun `get comments by post id emit no response then loading then collect successful response data`() = runTest {

        every {
            useCase.getCommentsByPostId(any())
        } returns flowOf(
            ResponseData.Success(
                listOf(
                    PhotoPostComments(
                        id = 1,
                        content = "content"
                    )
                )
            )
        )

        viewmodel = PhotoDetailViewModel("1", useCase)

        viewmodel.photoComments.test {

            assertThat(awaitItem()).isEqualTo(PhotoDetailUiState.Loading)

            val finalResult = awaitItem()

            assertTrue(finalResult is PhotoDetailUiState.Successful)
            assertThat(finalResult.comments).isNotEmpty()
        }
    }

    @Test
    fun `get comments by post id emit no response then loading then collect error data`() = runTest {

        every {
            useCase.getCommentsByPostId(any())
        } returns flowOf(
            ResponseData.Error("error")
        )

        viewmodel = PhotoDetailViewModel("1", useCase)

        viewmodel.photoComments.test {

            assertThat(awaitItem()).isEqualTo(PhotoDetailUiState.Loading)

            val finalResult = awaitItem()

            assertTrue(finalResult is PhotoDetailUiState.Error)
            assertThat(finalResult.message).isEqualTo("error")
        }
    }

    @Test
    fun `get comments by post id emit no response then loading then catch error thrown in flow`() = runTest {

        every {
            useCase.getCommentsByPostId(any())
        } returns flow{
            throw Exception("error from flow")
        }

        viewmodel = PhotoDetailViewModel("1", useCase)

        viewmodel.photoComments.test {

            assertThat(awaitItem()).isEqualTo(PhotoDetailUiState.Loading)

            val finalResult = awaitItem()

            assertTrue(finalResult is PhotoDetailUiState.Error)
            assertThat(finalResult.message).isNotEqualTo("error")
            assertThat(finalResult.message).isEqualTo("error from flow")
        }
    }
}