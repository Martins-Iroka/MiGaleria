package com.martdev.domain

import com.martdev.domain.login.UserLoginDataSource
import com.martdev.domain.login.UserLoginUseCase
import com.martdev.domain.login.userLoginUseCaseModule
import com.martdev.domain.registration.UserRegistrationDataSource
import com.martdev.domain.registration.UserRegistrationUseCase
import com.martdev.domain.registration.userRegistrationUseCaseModule
import com.martdev.domain.verification.UserVerificationDataSource
import com.martdev.domain.verification.UserVerificationUseCase
import com.martdev.domain.verification.userVerificationUseCaseModule
import io.mockk.mockkClass
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DomainKoinModuleTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            userLoginUseCaseModule,
            userRegistrationUseCaseModule,
            userVerificationUseCaseModule
        )
    }

    @get:Rule
    val mockRule = MockProviderRule.create {
        mockkClass(it)
    }

    @Test
    fun `should inject user login data source in login use case`() {

        val mock = declareMock<UserLoginDataSource>()
        val useCase = get<UserLoginUseCase>()
        assertNotNull(mock)
        assertNotNull(useCase)
        assertEquals(mock, useCase.userLoginDataSource)
    }

    @Test
    fun `should inject user registration data source in registration use case`() {

        val mock = declareMock<UserRegistrationDataSource>()
        val useCase = get<UserRegistrationUseCase>()
        assertNotNull(mock)
        assertNotNull(useCase)
        assertEquals(mock, useCase.userRegistrationDataSource)
    }

    @Test
    fun `should inject user verification data source in verification use case`() {

        val mock = declareMock<UserVerificationDataSource>()
        val useCase = get<UserVerificationUseCase>()
        assertNotNull(mock)
        assertNotNull(useCase)
        assertEquals(mock, useCase.userVerificationDataSource)
    }
}