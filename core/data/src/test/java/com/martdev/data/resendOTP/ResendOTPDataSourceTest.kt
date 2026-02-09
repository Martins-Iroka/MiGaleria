@file:Suppress("UnusedFlow")

package com.martdev.data.resendOTP

import com.martdev.common.NetworkResult
import com.martdev.domain.ResponseData
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.resendOTP.ResendOTPRemoteSource
import com.martdev.remote.resendOTP.ResendOTPResponseAPI
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
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

class ResendOTPDataSourceTest {

    @get:Rule
    val mockRule = MockKRule(this)

    @MockK
    private lateinit var resendOTPRemote: ResendOTPRemoteSource

    private lateinit var dataSource: ResendOTPDataSource

    @Before
    fun setup() {
        dataSource = resendOTPDataSource(resendOTPRemote)
    }

    @Test
    fun `resend otp return success and response data is success`() = runTest {
        every {
            resendOTPRemote.resendOTP(any())
        } returns flowOf(
            NetworkResult.Success(ResponseDataPayload(ResendOTPResponseAPI("emailID")))
        )

        val result = dataSource.resendOTP("email").first()

        assertTrue(result is ResponseData.Success)
        assertNotNull(result.data)
        assertEquals("emailID", result.data)

        verify {
            resendOTPRemote.resendOTP(any())
        }
    }

    @Test
    fun `resend otp return bad request and response data is error`() = runTest {
        every {
            resendOTPRemote.resendOTP(any())
        } returns flowOf(
            NetworkResult.Failure.BadRequest()
        )

        val result = dataSource.resendOTP("email").first()
        assertTrue(result is ResponseData.Error)
        assertEquals("Bad Request", result.message)
    }

    @Test
    fun `resend otp return internal server error and response data is error`() = runTest {
        every {
            resendOTPRemote.resendOTP(any())
        } returns flowOf(
            NetworkResult.Failure.InternalServerError()
        )

        val result = dataSource.resendOTP("email").first()
        assertTrue(result is ResponseData.Error)
        assertEquals("Internal Server Error", result.message)
    }
}