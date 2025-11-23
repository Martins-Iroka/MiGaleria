package com.martdev.login

import app.cash.turbine.test
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class UserLoginViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: UserLoginUseCase

    private lateinit var viewmodel: UserLoginViewModel

    @Before
    fun setup() {
        clearMocks(useCase)
        viewmodel = UserLoginViewModel(useCase)
    }

    @Test
    fun `login user should emit Loading then Success when use case succeeds`() = runTest {

        coEvery { useCase(any(), any()) } returns flowOf(ResponseData.Success(null))

        viewmodel.loginRes.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.loginUser("email", "123456")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Success<*>>(finalState)

            expectNoEvents()
        }
    }

    @Test
    fun `login user should emit Loading then Error when use case succeeds`() = runTest {

        coEvery { useCase(any(), any()) } returns flowOf(ResponseData.Error("Error"))

        viewmodel.loginRes.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.loginUser("email", "123456")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Error>(finalState)
            assertEquals("Error", finalState.message)

            expectNoEvents()
        }
    }
}