package com.martdev.verification

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.martdev.domain.ResponseData
import com.martdev.domain.verification.UserVerificationDataSource
import com.martdev.domain.verification.UserVerificationUseCase
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

class UserVerificationKoinTest : KoinTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val mockK = MockKRule(this)

    @MockK
    private lateinit var useCase: UserVerificationUseCase

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    factory<UserVerificationDataSource> { MockUserVerificationDataSource() }
                    factory { useCase }
                    viewModelOf(::UserVerificationViewModel)
                }
            )
        }
    }

    @Test
    fun clickVerifyButton_triggerViewModel() {
        every {
            useCase(any(), any())
        } returns flowOf(ResponseData.Loading)

        with(composeTestRule) {
            setContent {
                UserVerificationScreen()
            }

            onNodeWithTag(UserVerificationTag.VerifyButtonTag.toString())
                .assertExists().assertIsDisplayed().performClick()

            onNodeWithTag(
                UserVerificationTag.LoadingIndicatorTag.toString()
            ).assertExists().assertIsDisplayed()
        }
    }
}