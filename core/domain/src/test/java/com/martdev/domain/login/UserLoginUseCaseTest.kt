package com.martdev.domain.login

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
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class UserLoginUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var userLoginDataSource: UserLoginDataSource

    private lateinit var useCase: UserLoginUseCase

    @Before
    fun setup() {
        clearMocks(userLoginDataSource)
        useCase = UserLoginUseCase(userLoginDataSource)
    }

    @Test
    fun `call login user response data is successful`() = runTest {

        val requestSlot = slot<UserLoginDataRequest>()
        val request = UserLoginDataRequest("email", "password")
        every { userLoginDataSource.loginUser(capture(requestSlot)) } answers {
            assertEquals(request, requestSlot.captured)
            assertEquals(request.email, requestSlot.captured.email)
            assertEquals(request.password, requestSlot.captured.password)
            flowOf(ResponseData.Success(null))
        }

        val r = useCase("email", "password").first()

        assertTrue(r is ResponseData.Success)
        assertEquals(null, r.data)

        verify {
            userLoginDataSource.loginUser(any())
        }
    }

    @Test
    fun `call login user response data is error`() = runTest {

        val requestSlot = slot<UserLoginDataRequest>()
        val request = UserLoginDataRequest("email", "password")
        every { userLoginDataSource.loginUser(capture(requestSlot)) } answers {
            assertEquals(request, requestSlot.captured)
            assertEquals(request.email, requestSlot.captured.email)
            assertEquals(request.password, requestSlot.captured.password)
            flowOf(ResponseData.Error("error"))
        }

        val r = useCase("email", "password").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("error", r.message)

        verify {
            userLoginDataSource.loginUser(any())
        }
    }
}