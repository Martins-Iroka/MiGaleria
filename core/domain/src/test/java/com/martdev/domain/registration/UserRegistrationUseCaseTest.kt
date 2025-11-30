@file:Suppress("UnusedFlow")

package com.martdev.domain.registration

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

class UserRegistrationUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var source: UserRegistrationDataSource

    private lateinit var useCase: UserRegistrationUseCase

    @Before
    fun setup() {
        clearMocks(source)
        useCase = UserRegistrationUseCase(source)
    }

    @Test
    fun `register user response data is successful`() = runTest {
        val requestSlot = slot<UserRegistrationDataRequest>()
        val request = UserRegistrationDataRequest(
            "email", "martdev", "password"
        )
        every {
            source.registerUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            assertEquals("email", requestSlot.captured.email)
            assertEquals("martdev", requestSlot.captured.username)
            assertEquals("password", requestSlot.captured.password)
            flowOf(ResponseData.Success(null))
        }

        val r = useCase("email", "password", "martdev").first()

        assertTrue(r is ResponseData.Success)
        assertEquals(null, r.data)

        verify {
            source.registerUser(any())
        }
    }

    @Test
    fun `register user response data is error`() = runTest {
        val requestSlot = slot<UserRegistrationDataRequest>()
        val request = UserRegistrationDataRequest(
            "email", "martdev", "password"
        )
        every {
            source.registerUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            assertEquals("email", requestSlot.captured.email)
            assertEquals("martdev", requestSlot.captured.username)
            assertEquals("password", requestSlot.captured.password)
            flowOf(ResponseData.Error("Error"))
        }

        val r = useCase("email", "password", "martdev").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("Error", r.message)

        verify {
            source.registerUser(any())
        }
    }
}