package com.martdev.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginDataSource
import com.martdev.domain.login.UserLoginUseCase
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

class UserLoginKoinTest : KoinTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var useCase: UserLoginUseCase

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    factory<UserLoginDataSource> { MockLoginDataSourceImp() }
                    factory { useCase }
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