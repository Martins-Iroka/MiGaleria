package com.martdev.login

import app.cash.turbine.test
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginUseCase
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
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
        viewmodel = UserLoginViewModel(useCase)
    }

    @Test
    fun `login user should emit Loading then Success when use case return success`() = runTest {

        val emailSlot = slot<String>()
        val passwordSlot = slot<String>()

        every { useCase(capture(emailSlot), capture(passwordSlot)) } answers {
            assertEquals("email", emailSlot.captured)
            assertEquals("password", passwordSlot.captured)
            flowOf(ResponseData.Success(null))
        }

        viewmodel.loginRes.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.loginUser("email", "password")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Success<*>>(finalState)

            expectNoEvents()
        }
    }

    @Test
    fun `login user should emit Loading then Error when use case return error`() = runTest {

        val emailSlot = slot<String>()
        val passwordSlot = slot<String>()
        every { useCase(capture(emailSlot), capture(passwordSlot)) } answers {
            assertEquals("email", emailSlot.captured)
            assertEquals("password", passwordSlot.captured)
            flowOf(ResponseData.Error("Error"))
        }

        viewmodel.loginRes.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.loginUser("email", "password")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Error>(finalState)
            assertEquals("Error", finalState.message)

            expectNoEvents()
        }
    }
}