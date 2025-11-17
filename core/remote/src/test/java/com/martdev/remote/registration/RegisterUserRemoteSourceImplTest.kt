package com.martdev.remote.registration

import com.martdev.remote.AUTH_REGISTER_PATH
import com.martdev.remote.NetworkResult
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

@Suppress("UnusedFlow")
class RegisterUserRemoteSourceImplTest {

    private val registerUserResponseJson = "registerUserResponsePayload.json"
    private val request = RegisterUserRequestPayload(
        username = "martdev",
        email = "ik@gmail.com",
        password = "123456"
    )
    private val authRegisterPath = "/v1$AUTH_REGISTER_PATH"

    @Test
    fun postRegisterUserRequest_returnSuccessRegisterUserResponse() = runTest {
        val client = getMockClient(registerUserResponseJson, path = authRegisterPath)
        mockkConstructor(RegisterUserRemoteSourceImpl::class)
        val requestSlot = slot<RegisterUserRequestPayload>()
        every {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }

        val result =
            RegisterUserRemoteSourceImpl(client).registerUser(request).first()
        if (result is NetworkResult.Success) {
            assertTrue(result.data.data.token.isNotEmpty())
            assertEquals("token", result.data.data.token)
        }
    }

    @Test
    fun postRegisterUserRequest_returnBadRequest() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.BadRequest, path = authRegisterPath)
        mockkConstructor(RegisterUserRemoteSourceImpl::class)
        val requestSlot = slot<RegisterUserRequestPayload>()
        every {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }
        val result =
            RegisterUserRemoteSourceImpl(client).registerUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Bad Request", result.error)
        }

        verify {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(any())
        }
    }

    @Test
    fun postRegisterUserRequest_returnUnauthorized() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.Unauthorized)
        mockkConstructor(RegisterUserRemoteSourceImpl::class)
        val requestSlot = slot<RegisterUserRequestPayload>()
        every {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }
        val result =
            RegisterUserRemoteSourceImpl(client).registerUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Unauthorized", result.error)
        }

        verify {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(any())
        }
    }

    @Test
    fun postRegisterUserRequest_returnInternalServerError() = runTest {
        val client = getMockClient(statusCode = HttpStatusCode.InternalServerError, path = authRegisterPath)
        mockkConstructor(RegisterUserRemoteSourceImpl::class)
        val requestSlot = slot<RegisterUserRequestPayload>()
        every {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            callOriginal()
        }
        val result =
            RegisterUserRemoteSourceImpl(client).registerUser(request).first()
        if (result is NetworkResult.Failure) {
            assertEquals("Internal Server Error", result.error)
        }

        verify {
            constructedWith<RegisterUserRemoteSourceImpl>(EqMatcher(client))
                .registerUser(any())
        }
    }
}