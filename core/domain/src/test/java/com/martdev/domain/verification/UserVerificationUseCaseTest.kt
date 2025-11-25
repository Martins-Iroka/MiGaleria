package com.martdev.domain.verification

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
class UserVerificationUseCaseTest {

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var source: UserVerificationDataSource

    private lateinit var useCase: UserVerificationUseCase

    @Before
    fun setup() {
        clearMocks(source)
        useCase = UserVerificationUseCase(source)
    }

    @Test
    fun `verify code response data is successful`() = runTest {
        val request = UserVerificationDataRequest("code", "email")
        val requestSlot = slot<UserVerificationDataRequest>()

        every {
            source.verifyUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            assertEquals("code", requestSlot.captured.code)
            assertEquals("email", requestSlot.captured.email)
            flowOf(ResponseData.Success(null))
        }

        val r = useCase("code", "email").first()

        assertTrue(r is ResponseData.Success)
        assertEquals(null, r.data)

        verify {
            source.verifyUser(any())
        }
    }

    @Test
    fun `verify code response data is error`() = runTest {
        val request = UserVerificationDataRequest("code", "email")
        val requestSlot = slot<UserVerificationDataRequest>()

        every {
            source.verifyUser(capture(requestSlot))
        } answers {
            assertEquals(request, requestSlot.captured)
            assertEquals("code", requestSlot.captured.code)
            assertEquals("email", requestSlot.captured.email)
            flowOf(ResponseData.Error("error"))
        }

        val r = useCase("code", "email").first()

        assertTrue(r is ResponseData.Error)
        assertEquals("error", r.message)

        verify {
            source.verifyUser(any())
        }
    }
}