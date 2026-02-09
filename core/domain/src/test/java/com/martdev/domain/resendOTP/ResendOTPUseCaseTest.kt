@file:Suppress("UnusedFlow")

package com.martdev.domain.resendOTP

import com.martdev.domain.ResponseData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ResendOTPUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var source: ResendOTPDataSource

    private lateinit var useCase: ResendOTPUseCase

    @Before
    fun setup() {
        clearMocks(source)
        useCase = ResendOTPUseCase(source)
    }

    @Test
    fun `resend otp response data is successful`() = runTest {
        val requestSlot = slot<String>()
        val email = "test@example.com"

        every {
            source.resendOTP(capture(requestSlot))
        } answers {
            assertEquals(email, requestSlot.captured)
            flowOf(ResponseData.Success("emailID"))
        }

        val result = useCase(email).first()

        assertTrue(result is ResponseData.Success)
        assertNotNull(result.data)
        assertEquals("emailID", result.data)

        verify {
            source.resendOTP(any())
        }
    }

    @Test
    fun `resend otp response data is error`() = runTest {
        val requestSlot = slot<String>()
        val email = "test@example.com"

        every {
            source.resendOTP(capture(requestSlot))
        } answers {
            assertEquals(email, requestSlot.captured)
            flowOf(ResponseData.Error("error"))
        }

        val result = useCase(email).first()

        assertTrue(result is ResponseData.Error)
        assertEquals("error", result.message)

        verify {
            source.resendOTP(any())
        }
    }
}