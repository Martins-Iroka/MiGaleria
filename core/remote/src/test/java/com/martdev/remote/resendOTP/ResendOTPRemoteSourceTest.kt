package com.martdev.remote.resendOTP

import com.martdev.common.NetworkResult
import com.martdev.remote.client.RESEND_OTP_PATH
import com.martdev.remote.util.badRequestJsonResponse
import com.martdev.remote.util.badRequestMessage
import com.martdev.remote.util.getMockClient
import com.martdev.remote.util.internalServerError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResendOTPRemoteSourceTest {

    private val resendOTPResponseJson = "resendOTPResponse.json"
    private val request = ResendOTPRequestAPI("test@example.com")
    private val resendOTPPath = "/v1$RESEND_OTP_PATH"

    @Test
    fun postResendOTPRequest_returnSuccessOTPResponse() = runTest {
        val client = getMockClient(resendOTPResponseJson, path = resendOTPPath)
        val resendOTPSource = resendOTPRemoteSource(client)
        val result = resendOTPSource.resendOTP(request).first()

        assertTrue(result is NetworkResult.Success)
        assertEquals("emailID", result.data.data.emailId)
    }

    @Test
    fun postResendOTPRequest_returnBadRequest() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = resendOTPPath, json = badRequestJsonResponse)
        val resendOTPSource = resendOTPRemoteSource(client)
        val result = resendOTPSource.resendOTP(request).first()

        assertTrue(result is NetworkResult.Failure)
        assertEquals(badRequestMessage, result.error)
    }

    @Test
    fun postResendOTPRequest_returnInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = resendOTPPath)
        val resendOTPSource = resendOTPRemoteSource(client)
        val result = resendOTPSource.resendOTP(request).first()

        assertTrue(result is NetworkResult.Failure)
        assertEquals(internalServerError, result.error)
    }
}