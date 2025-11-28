package com.martdev.registration

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationDataSource
import com.martdev.domain.registration.UserRegistrationUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.test.KoinTest

class UserRegistrationKoinTest : KoinTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var dataSource: UserRegistrationDataSource

    @MockK
    private lateinit var useCase: UserRegistrationUseCase

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single { dataSource }
                    factory { useCase }
                    viewModelOf(::UserRegistrationViewModel)
                }
            )
        }
    }

    @Test
    fun clickSignUpButton_triggersViewModel() {
        every {
            useCase(any(), any(), any())
        } returns flowOf(ResponseData.Loading)

        with(composeTestRule) {
            setContent {
                UserRegistrationScreen()
            }

            onNodeWithTag(
                UserRegistrationTag.SignUpButton.toString()
            ).performClick()

            onNodeWithTag(
                UserRegistrationTag.LoadingIndicator.toString()
            ).assertExists().assertIsDisplayed()
        }
    }
}