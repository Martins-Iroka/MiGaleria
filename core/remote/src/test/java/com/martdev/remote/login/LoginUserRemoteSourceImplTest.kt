@file:Suppress("UnusedFlow")

package com.martdev.remote.login

import com.martdev.remote.AUTH_LOGIN_PATH
import com.martdev.remote.NetworkResult
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

class LoginUserRemoteSourceImplTest {

    private val loginUserResponseJson = "loginUserResponsePayload.json"
    private val request = LoginUserRequestPayload(
        email = "test@gmail.com",
        password = "123456"
    )
    private val authLoginPath = "/v1$AUTH_LOGIN_PATH"

    @Test
    fun postLoginUserRequest_returnSuccessLoginUserResponse() = runTest {
        val client = getMockClient(loginUserResponseJson, path = authLoginPath)
        mockkConstructor(LoginUserRemoteSourceImpl::class)
        val requestSlot = slot<LoginUserRequestPayload>()
        every {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = LoginUserRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Success) {
            assertEquals("access_token",result.data.data.accessToken)
            assertEquals("refresh_token",result.data.data.refreshToken)
        }

        verify {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLoginUserRequest_returnBadRequest() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = authLoginPath, json = badRequestJsonResponse)
        mockkConstructor(LoginUserRemoteSourceImpl::class)
        val requestSlot = slot<LoginUserRequestPayload>()
        every {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = LoginUserRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals(badRequestMessage, result.error)
        }

        verify {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLoginUserRequest_returnUnauthorized() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(LoginUserRemoteSourceImpl::class)
        val requestSlot = slot<LoginUserRequestPayload>()
        every {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = LoginUserRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Unauthorized", result.error)
        }

        verify {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLoginUserRequest_returnInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = authLoginPath)
        mockkConstructor(LoginUserRemoteSourceImpl::class)
        val requestSlot = slot<LoginUserRequestPayload>()
        every {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = LoginUserRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<LoginUserRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }
}