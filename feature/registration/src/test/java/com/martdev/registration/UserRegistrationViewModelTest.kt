package com.martdev.registration

import app.cash.turbine.test
import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationDataResponse
import com.martdev.domain.registration.UserRegistrationUseCase
import com.martdev.test_shared.MainCoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class UserRegistrationViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var useCase: UserRegistrationUseCase

    private lateinit var viewmodel: UserRegistrationViewModel

    @Before
    fun setup() {
        viewmodel = UserRegistrationViewModel(useCase)
    }

    @Test
    fun `register user should emit loading then success when use case returns succeeds`() = runTest {

        val emailSlot = slot<String>()
        val passwordSlot = slot<String>()
        val usernameSlot = slot<String>()
        every {
            useCase(capture(emailSlot), capture(passwordSlot), capture(usernameSlot))
        } answers {
            assertEquals("email", emailSlot.captured)
            assertEquals("password", passwordSlot.captured)
            assertEquals("martdev", usernameSlot.captured)
            flowOf(
                ResponseData.Success(UserRegistrationDataResponse("emailID"))
            )
        }

        viewmodel.response.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.registerUser("email", "password", "martdev")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Success<UserRegistrationDataResponse>>(finalState)
            assertNotNull(finalState.data)
            assertEquals("emailID", finalState.data!!.emailID)

            expectNoEvents()
        }
    }

    @Test
    fun `register user should emit loading then error when use case returns error`() = runTest {

        val emailSlot = slot<String>()
        val passwordSlot = slot<String>()
        val usernameSlot = slot<String>()
        every {
            useCase(capture(emailSlot), capture(passwordSlot), capture(usernameSlot))
        } answers {
            assertEquals("email", emailSlot.captured)
            assertEquals("password", passwordSlot.captured)
            assertEquals("martdev", usernameSlot.captured)
            flowOf(
                ResponseData.Error("error")
            )
        }

        viewmodel.response.test {
            assertEquals(ResponseData.NoResponse, awaitItem())

            viewmodel.registerUser("email", "password", "martdev")

            assertEquals(ResponseData.Loading, awaitItem())

            val finalState = awaitItem()
            assertIs<ResponseData.Error>(finalState)
            assertEquals("error", finalState.message)

            expectNoEvents()
        }
    }
}