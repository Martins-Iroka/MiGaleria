package com.martdev.remote.verification

import com.martdev.common.NetworkResult
import com.martdev.remote.AUTH_VERIFY_PATH
import com.martdev.remote.util.badRequestJsonResponse
import com.martdev.remote.util.badRequestMessage
import com.martdev.remote.util.getMockClient
import io.ktor.http.HttpStatusCode
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
class UserVerificationRemoteSourceImplTest {

    private val verifyUserResponseJson = "verifyUserResponsePayload.json"
    private val request = UserVerificationRequestPayload(
        code = "code",
        email = "t@g.com",
        token = "token"
    )

    private val authVerifyUserPath = "/v1$AUTH_VERIFY_PATH"

    @Test
    fun postVerifyUserRequest_returnSuccessUserVerifiedResponse() = runTest {
        val client = getMockClient(verifyUserResponseJson, path = authVerifyUserPath)
        mockkConstructor(UserVerificationRemoteSourceImpl::class)
        val requestSlot = slot<UserVerificationRequestPayload>()
        every {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result =
            UserVerificationRemoteSourceImpl(client).verifyUser(request).first()

        if (result is NetworkResult.Success) {
            assertEquals("verified", result.data.data.status)
        }

        verify {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(any())
        }
    }

    @Test
    fun postVerifyUserRequest_returnBadRequest() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = authVerifyUserPath, json = badRequestJsonResponse)
        mockkConstructor(UserVerificationRemoteSourceImpl::class)
        val requestSlot = slot<UserVerificationRequestPayload>()
        every {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result =
            UserVerificationRemoteSourceImpl(client).verifyUser(request).first()

        if (result is NetworkResult.Failure) {
            assertEquals(badRequestMessage, result.error)
        }

        verify {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(any())
        }
    }

    @Test
    fun postVerifyUserRequest_returnUnauthorized() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(UserVerificationRemoteSourceImpl::class)
        val requestSlot = slot<UserVerificationRequestPayload>()
        every {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result =
            UserVerificationRemoteSourceImpl(client).verifyUser(request).first()

        if (result is NetworkResult.Failure) {
            assertEquals("Unauthorized", result.error)
        }

        verify {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(any())
        }
    }

    @Test
    fun postVerifyUserRequest_returnInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = authVerifyUserPath)
        mockkConstructor(UserVerificationRemoteSourceImpl::class)
        val requestSlot = slot<UserVerificationRequestPayload>()
        every {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result =
            UserVerificationRemoteSourceImpl(client).verifyUser(request).first()

        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<UserVerificationRemoteSourceImpl>(EqMatcher(client))
                .verifyUser(any())
        }
    }
}