package com.martdev.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.martdev.domain.ResponseData
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserLoginTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun enterEmail_Password_click_loginButton() = runTest {
        with(composeTestRule){
            setContent {
                UserLogin(loginUserClick = { e, p ->
                    assertEquals("t@gmail.com", e)
                    assertEquals("123456", p)
                })
            }

            onNodeWithTag(UserLoginTag.LoginScreenTag.toString()).assertExists().assertIsDisplayed()
            onNodeWithTag(UserLoginTag.EnterEmailTag.toString())
                .assertExists().assertIsDisplayed().performTextInput("t@gmail.com")
            onNodeWithTag(UserLoginTag.EnterPasswordTag.toString())
                .assertExists().assertIsDisplayed().performTextInput("123456")
            onNodeWithTag(UserLoginTag.LoginButtonTag.toString()).assertExists()
                .assertIsDisplayed().performClick()
        }
    }

    @Test
    fun testForgetPasswordClicked() = runTest {
        with(composeTestRule) {
            var forgetPasswordClicked = false
            setContent {
                UserLogin(forgetPasswordClick = {
                    forgetPasswordClicked = true
                })
            }

            onNodeWithTag(UserLoginTag.ForgotPasswordButtonTag.toString())
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            assertTrue(forgetPasswordClicked, "The forgetPasswordClick lambda was not called.")
        }
    }

    @Test
    fun testSignUpButtonClicked() = runTest {
        with(composeTestRule) {
            var signupClicked = false
            setContent {
                UserLogin(signupClick = {
                    signupClicked = true
                })
            }

            onNodeWithTag(UserLoginTag.SignUpTextButtonTag.toString())
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            assertTrue(signupClicked)
        }
    }

    @Test
    fun setResponseDataToLoading_showCircularProgressIndicator() = runTest {
        with(composeTestRule) {
            setContent {
                UserLogin(
                    responseData = ResponseData.Loading
                )
            }

            onNodeWithTag(UserLoginTag.LoginCircularTag.toString()).assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun setResponseDataToError_showsSnackbarWithMessage() = runTest {
        with(composeTestRule) {
            val errorMessage = "Invalid credentials"

            setContent {
                UserLogin(
                    responseData = ResponseData.Error(errorMessage)
                )
            }

            onNodeWithText(errorMessage)
                .assertExists()
                .assertIsDisplayed()
        }
    }

}