package com.martdev.registration

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.test.KoinTest

class UserRegistrationKoinTest : KoinTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val useCase: UserRegistrationUseCase = mockk()

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            modules(
                module {
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
                UserRegistrationComposable{}
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