package com.martdev.registration

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

class UserRegistrationComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun enterEmail_Password_Username_clickSignUpButton() {
        with(composeTestRule) {
            setContent {
                UserRegistration(
                    signUpUserClicked = { e, p, u ->
                        assertEquals("email", e)
                        assertEquals("password", p)
                        assertEquals("username", u)
                    }
                )
            }

            onNodeWithTag(
                UserRegistrationTag.RegistrationScreen.toString()
            ).assertExists().assertIsDisplayed()
            onNodeWithTag(
                UserRegistrationTag.EmailInput.toString()
            ).assertExists().assertIsDisplayed().performTextInput("email")
            onNodeWithTag(
                UserRegistrationTag.PasswordInput.toString()
            ).assertExists().assertIsDisplayed().performTextInput("password")
            onNodeWithTag(
                UserRegistrationTag.UsernameInput.toString()
            ).assertExists().assertIsDisplayed().performTextInput("username")
            onNodeWithTag(
                UserRegistrationTag.SignUpButton.toString()
            ).assertExists().assertIsDisplayed().performClick()
        }
    }

    @Test
    fun testLoginButtonClicked() {
        with(composeTestRule) {
            var loginClicked = false
            setContent {
                UserRegistration(
                    loginUserClicked = {
                        loginClicked = true
                    }
                )
            }

            onNodeWithTag(UserRegistrationTag.LoginButton.toString())
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            assertTrue(loginClicked)
        }
    }

    @Test
    fun setResponseDataToLoading_showCircularProgressIndicator() {
        with(composeTestRule) {
            setContent {
                UserRegistration(
                    responseData = ResponseData.Loading
                )
            }

            onNodeWithTag(
                UserRegistrationTag.LoadingIndicator.toString()
            ).assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun setResponseDataToError_showSnackBarWithMessage() = runTest {
        with(composeTestRule) {
            setContent {
                UserRegistration(
                    responseData = ResponseData.Error("error")
                )
            }

            onNodeWithText("error").assertExists().assertIsDisplayed()
        }
    }
}