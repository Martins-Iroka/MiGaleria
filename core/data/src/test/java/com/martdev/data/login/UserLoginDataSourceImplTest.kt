@file:Suppress("UnusedFlow")

package com.martdev.data.login

import com.martdev.common.NetworkResult
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginDataRequest
import com.martdev.domain.login.UserLoginDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.login.LoginUserRemoteSource
import com.martdev.remote.login.LoginUserResponsePayload
import com.martdev.remote.login.LogoutUserRequest
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UserLoginDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var remote: LoginUserRemoteSource

    @MockK
    private lateinit var tokenStorage: TokenStorage

    private lateinit var userDatasource: UserLoginDataSource

    private val request = UserLoginDataRequest(
        email = "t@gmail.com",
        password = "12345"
    )

    private val logoutRequest = LogoutUserRequest("refresh_token")

    @Before
    fun setup() {
        clearAllMocks()
        userDatasource = UserLoginDataSourceImpl(remote, tokenStorage)
    }

    @Test
    fun loginUser_confirmResponseDataIsSuccessful() = runTest {

        every { remote.loginUser(any()) } returns flowOf(
            NetworkResult.Success(ResponseDataPayload(
                data = LoginUserResponsePayload("access_token", "refresh_token")
            ))
        )

        coEvery { tokenStorage.saveTokens(any()) } just Runs

        val r = userDatasource.loginUser(request).first()

        assertTrue(r is ResponseData.Success)

        coVerify {
            remote.loginUser(any())
            tokenStorage.saveTokens(any())
        }
    }

    @Test
    fun logintUser_responseBadRequest_confirmResponseDataIsError() = runTest {

        every {
            remote.loginUser(any())
        } returns flowOf(NetworkResult.Failure.BadRequest())

        val r = userDatasource.loginUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Bad Request", r.message)

        coVerify {
            remote.loginUser(any())
        }
    }

    @Test
    fun logintUser_responseInternalServerError_confirmResponseDataIsError() = runTest {

        every {
            remote.loginUser(any())
        } returns flowOf(NetworkResult.Failure.InternalServerError())

        val r = userDatasource.loginUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)

        coVerify {
            remote.loginUser(any())
        }
    }

    @Test
    fun logoutUser_responseSuccessful_returnResponseDataIsSuccessful() = runTest {
        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken("access_token", "refresh_token"))

        every {
            remote.logoutUser(any())
        } returns flowOf(NetworkResult.Success(Unit))

        coEvery { tokenStorage.clearTokens() } just Runs

        val r = userDatasource.logoutUser().first()

        assertTrue(r is ResponseData.Success)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.logoutUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun logoutUser_responseBadRequest_returnResponseDataIsError() = runTest {
        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken("access_token", "refresh_token"))

        every {
            remote.logoutUser(any())
        } returns flowOf(NetworkResult.Failure.BadRequest())

        coEvery { tokenStorage.clearTokens() } just Runs

        val r = userDatasource.logoutUser().first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Bad Request", r.message)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.logoutUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun logoutUser_responseInternalServerError_returnResponseDataIsError() = runTest {
        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken("access_token", "refresh_token"))

        every {
            remote.logoutUser(any())
        } returns flowOf(NetworkResult.Failure.InternalServerError())

        coEvery { tokenStorage.clearTokens() } just Runs

        val r = userDatasource.logoutUser().first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.logoutUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun logoutUser_throwIllegalStateException_returnResponseDataIsError() = runTest() {
        every {
            tokenStorage.getTokens()
        } throws IllegalStateException("Error")

        coEvery { tokenStorage.clearTokens() } just Runs

        val e = assertFailsWith<IllegalStateException> {
            userDatasource.logoutUser().first()
        }

        assertEquals("Error", e.message)
        try {
            userDatasource.logoutUser()
        } catch (e: Exception) {
            assertTrue(e is IllegalStateException)
            assertEquals("No refresh token found to log out.", e.message)
        }

        coVerify {
            tokenStorage.getTokens()
            tokenStorage.clearTokens()
        }
    }
}