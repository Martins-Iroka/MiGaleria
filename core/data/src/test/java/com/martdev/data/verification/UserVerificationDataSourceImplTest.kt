@file:Suppress("UnusedFlow")

package com.martdev.data.verification

import com.martdev.common.NetworkResult
import com.martdev.domain.ResponseData
import com.martdev.domain.verification.UserVerificationDataRequest
import com.martdev.domain.verification.UserVerificationDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.verification.UserVerificationRemoteSource
import com.martdev.remote.verification.UserVerificationResponsePayload
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
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

class UserVerificationDataSourceImplTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var remote: UserVerificationRemoteSource

    @MockK
    private lateinit var tokenStorage: TokenStorage

    private lateinit var dataSource: UserVerificationDataSource

    private val request = UserVerificationDataRequest(
        "code", "email"
    )

    @Before
    fun setup() {
        clearAllMocks()
        dataSource = UserVerificationDataSourceImpl(remote, tokenStorage)
    }

    @Test
    fun `call verify user response is successful returns response as success`() = runTest {

        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken(verificationToken = "token"))

        every {
            remote.verifyUser(any())
        } returns flowOf(
            NetworkResult.Success(ResponseDataPayload(UserVerificationResponsePayload("verified")))
        )

        coEvery {
            tokenStorage.clearTokens()
        } just Runs

        val r = dataSource.verifyUser(request).first()

        assertTrue(r is ResponseData.Success)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.verifyUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun `call verify user response is bad request returns response as error`() = runTest {

        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken(verificationToken = "token"))

        every {
            remote.verifyUser(any())
        } returns flowOf(
            NetworkResult.Failure.BadRequest()
        )

        coEvery {
            tokenStorage.clearTokens()
        } just Runs

        val r = dataSource.verifyUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Bad Request", r.message)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.verifyUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun `call verify user response is not found returns response as error`() = runTest {

        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken(verificationToken = "token"))

        every {
            remote.verifyUser(any())
        } returns flowOf(
            NetworkResult.Failure.NotFound()
        )

        coEvery {
            tokenStorage.clearTokens()
        } just Runs

        val r = dataSource.verifyUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Not Found", r.message)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.verifyUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun `call verify user response is internal server error returns response as error`() = runTest {

        every {
            tokenStorage.getTokens()
        } returns flowOf(AuthToken(verificationToken = "token"))

        every {
            remote.verifyUser(any())
        } returns flowOf(
            NetworkResult.Failure.InternalServerError()
        )

        coEvery {
            tokenStorage.clearTokens()
        } just Runs

        val r = dataSource.verifyUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)

        coVerifyOrder {
            tokenStorage.getTokens()
            remote.verifyUser(any())
            tokenStorage.clearTokens()
        }
    }

    @Test
    fun `call verify user and throw illegalException`() = runTest {

        every {
            tokenStorage.getTokens()
        } throws IllegalStateException("Error")

        coEvery {
            tokenStorage.clearTokens()
        } just Runs

        val r = assertFailsWith<IllegalStateException> {
            dataSource.verifyUser(request).first()
        }

        assertEquals("Error", r.message)

        coVerifyOrder {
            tokenStorage.getTokens()
            tokenStorage.clearTokens()
        }
    }
}