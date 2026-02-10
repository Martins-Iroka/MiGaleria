package com.martdev.verification

import app.cash.turbine.test
import com.martdev.domain.ResponseData
import com.martdev.domain.resendOTP.ResendOTPUseCase
import com.martdev.domain.verification.UserVerificationUseCase
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UserVerificationViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: UserVerificationUseCase

    @MockK
    private lateinit var resendOTPUseCase: ResendOTPUseCase

    private lateinit var viewmodel: UserVerificationViewModel

    @Before
    fun setUp() {
        viewmodel = UserVerificationViewModel(useCase, resendOTPUseCase)
    }
    val codeSlot = slot<String>()
    val emailIDSlot = slot<String>()

    @Test
    fun `verify code should emit loading then success when use case returns success`() = runTest {

        every {
            useCase(capture(codeSlot), capture((emailIDSlot)))
        } answers {
            assertEquals("code", codeSlot.captured)
            assertEquals("emailID", emailIDSlot.captured)
            flowOf(
                ResponseData.Success(null)
            )
        }

        viewmodel.response.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.verifyCode("code", "emailID")

            assertEquals(ResponseData.Loading, awaitItem())

            assertIs<ResponseData.Success<*>>(awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun `verify code should emit loading then error when use case returns error`() = runTest {

        every {
            useCase(capture(codeSlot), capture(emailIDSlot))
        } answers {
            assertEquals("code", codeSlot.captured)
            assertEquals("emailID", emailIDSlot.captured)
            flowOf(
                ResponseData.Error("error")
            )
        }

        viewmodel.response.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.verifyCode("code", "emailID")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Error>(finalState)
            assertEquals("error", finalState.message)
        }
    }

    @Test
    fun `resend otp should return success when use case returns success`() = runTest {
        val emailSlot = slot<String>()
        every {
            resendOTPUseCase(capture(emailSlot))
        } answers {
            assertEquals("email", emailSlot.captured)
            flowOf(ResponseData.Success("emailID"))
        }

        viewmodel.resendOTPResponse.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.resendOTP("email")

            val finalState = awaitItem()
            assertIs<ResponseData.Success<String>>(finalState)
            assertEquals("emailID", finalState.data)
        }
    }

    @Test
    fun `resend otp should return error when use case returns error`() = runTest {
        val emailSlot = slot<String>()
        every {
            resendOTPUseCase(capture(emailSlot))
        } answers {
            assertEquals("email", emailSlot.captured)
            flowOf(ResponseData.Error("error"))
        }

        viewmodel.resendOTPResponse.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.resendOTP("email")

            val finalState = awaitItem()
            assertIs<ResponseData.Error>(finalState)
            assertEquals("error", finalState.message)
        }
    }
}