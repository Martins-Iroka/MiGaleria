package com.martdev.data.registration

import com.martdev.common.NetworkResult
import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationDataRequest
import com.martdev.domain.registration.UserRegistrationDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.registration.UserRegistrationRemoteSource
import com.martdev.remote.registration.UserRegistrationResponsePayload
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class UserRegistrationDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var remote: UserRegistrationRemoteSource

    @MockK
    private lateinit var tokenStorage: TokenStorage

    private lateinit var dataSource: UserRegistrationDataSource

    private val request = UserRegistrationDataRequest(
        "email",
        "martdev",
        password = "123456"
    )

    @Before
    fun setup() {
        clearAllMocks()
        dataSource = UserRegistrationDataSourceImpl(remote, tokenStorage)
    }

    @Test
    fun `register user, return success and response data is success`() = runTest {

        every { remote.registerUser(any()) } returns flowOf(
            NetworkResult.Success(ResponseDataPayload(UserRegistrationResponsePayload(token = "token")))
        )

        coEvery { tokenStorage.saveVerificationToken(any()) } just Runs

        val r = dataSource.registerUser(request).first()

        assertTrue(r is ResponseData.Success)

        coVerifyOrder {
            remote.registerUser(any())
            tokenStorage.saveVerificationToken(any())
        }
    }

    @Test
    fun `register user, return bad request and response data is error`() = runTest {

        every { remote.registerUser(any()) } returns flowOf(
            NetworkResult.Failure.BadRequest()
        )

        val r = dataSource.registerUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Bad Request", r.message)

        verify {
            remote.registerUser(any())
        }

        coVerify(atLeast = 0) {
            tokenStorage.saveVerificationToken(any())
        }
    }

    @Test
    fun `register user, return internal server error and response data is error`() = runTest {

        every { remote.registerUser(any()) } returns flowOf(
            NetworkResult.Failure.InternalServerError()
        )

        val r = dataSource.registerUser(request).first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Internal Server Error", r.message)

        verify {
            remote.registerUser(any())
        }

        coVerify(atLeast = 0) {
            tokenStorage.saveVerificationToken(any())
        }
    }
}