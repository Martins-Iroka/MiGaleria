package com.martdev.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginUseCase
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

class UserLoginKoinTest : KoinTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val useCase: UserLoginUseCase = mockk()

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            modules(
                module {
                    factory {useCase}
                    viewModelOf(::UserLoginViewModel)
                }
            )
        }
    }

    @Test
    fun clickLoginButton_triggerViewModel() {
        every {
            useCase(any(), any())
        } returns flowOf(ResponseData.Loading)

        with(composeTestRule) {
            setContent {
                UserLoginScreen()
            }

            onNodeWithTag(
                UserLoginTag.LoginButtonTag.toString()
            ).assertExists().assertIsDisplayed().performClick()

            onNodeWithTag(
                UserLoginTag.LoginCircularTag.toString()
            ).assertExists().assertIsDisplayed()
        }
    }
}