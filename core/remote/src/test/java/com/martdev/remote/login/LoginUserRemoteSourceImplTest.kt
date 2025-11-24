@file:Suppress("UnusedFlow")

package com.martdev.remote.login

import com.martdev.common.NetworkResult
import com.martdev.remote.client.AUTH_LOGIN_PATH
import com.martdev.remote.client.AUTH_LOGOUT_PATH
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
import kotlin.test.assertTrue

class LoginUserRemoteSourceImplTest {

    private val loginUserResponseJson = "loginUserResponsePayload.json"
    private val request = UserLoginRequestPayload(
        email = "test@gmail.com",
        password = "123456"
    )
    private val logoutRequest = LogoutUserRequest(
        refreshToken = "refreshToken"
    )
    private val authLoginPath = "/v1$AUTH_LOGIN_PATH"
    private val authLogoutPath = "/v1$AUTH_LOGOUT_PATH"

    @Test
    fun postLoginUserRequest_returnSuccessLoginUserResponse() = runTest {
        val client = getMockClient(loginUserResponseJson, path = authLoginPath)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<UserLoginRequestPayload>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Success) {
            assertEquals("access_token",result.data.data.accessToken)
            assertEquals("refresh_token",result.data.data.refreshToken)
        }

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLoginUserRequest_responseBadRequest() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = authLoginPath, json = badRequestJsonResponse)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<UserLoginRequestPayload>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals(badRequestMessage, result.error)
        }

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLoginUserRequest_responseNotFound() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.NotFound)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<UserLoginRequestPayload>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Not Found", result.error)
        }

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLoginUserRequest_responseInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = authLoginPath)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<UserLoginRequestPayload>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .loginUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .loginUser(any())
        }
    }

    @Test
    fun postLogoutUserRequest_responseNoContent() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.NoContent, path = authLogoutPath)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<LogoutUserRequest>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .logoutUser(capture(requestSlot))
        } answers {
            assertEquals(logoutRequest, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .logoutUser(logoutRequest).first()
        assertTrue(result is NetworkResult.Success)

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .logoutUser(any())
        }
    }

    @Test
    fun postLogoutUserRequest_responseBadRequest() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = authLogoutPath, json = badRequestJsonResponse)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<LogoutUserRequest>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .logoutUser(capture(requestSlot))
        } answers {
            assertEquals(logoutRequest, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .logoutUser(logoutRequest).first()
        assertTrue(result is NetworkResult.Failure)
        assertEquals(badRequestMessage, result.error)

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .logoutUser(any())
        }
    }

    @Test
    fun postLogoutUserRequest_responseInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = authLogoutPath)
        mockkConstructor(UserLoginRemoteSourceImpl::class)
        val requestSlot = slot<LogoutUserRequest>()
        every {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .logoutUser(capture(requestSlot))
        } answers {
            assertEquals(logoutRequest, requestSlot.captured)
            callOriginal()
        }

        val result = UserLoginRemoteSourceImpl(client)
            .logoutUser(logoutRequest).first()
        assertTrue(result is NetworkResult.Failure)
        assertEquals("Internal Server Error", result.error)

        verify {
            constructedWith<UserLoginRemoteSourceImpl>(EqMatcher(client))
                .logoutUser(any())
        }
    }
}