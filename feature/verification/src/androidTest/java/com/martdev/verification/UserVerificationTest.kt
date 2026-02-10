package com.martdev.verification

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.martdev.domain.ResponseData
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class UserVerificationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun enterCode_clickVerifyButton() {
        with(composeTestRule) {
            setContent {
                UserVerification(
                    emailID = "emailID",
                    verifyCode = { code, emailID ->
                    assertEquals("code", code)
                    assertEquals("emailID", emailID)
                })
            }

            onNodeWithTag(UserVerificationTag.UserVerificationScreenTag.toString())
                .assertExists().assertIsDisplayed()
            onNodeWithTag(UserVerificationTag.CodeInputTag.toString())
                .assertExists().assertIsDisplayed().performTextInput("code")
            onNodeWithTag(UserVerificationTag.VerifyButtonTag.toString()).assertExists()
                .assertIsDisplayed().performClick()
        }
    }

    @Test
    fun enterCode_resendOTP_clickVerifyButton() {
        with(composeTestRule) {
            setContent {
                UserVerification(
                    resendOTPResponse = ResponseData.Success("emailID"),
                    verifyCode = { code, emailID ->
                    assertEquals("code", code)
                    assertEquals("emailID", emailID)
                })
            }

            onNodeWithTag(UserVerificationTag.UserVerificationScreenTag.toString())
                .assertExists().assertIsDisplayed()
            onNodeWithTag(UserVerificationTag.CodeInputTag.toString())
                .assertExists().assertIsDisplayed().performTextInput("code")
            onNodeWithTag(UserVerificationTag.VerifyButtonTag.toString()).assertExists()
                .assertIsDisplayed().performClick()
        }
    }

    @Test
    fun setResponseDataToLoading_showCircularProgressIndicator() {
        with(composeTestRule) {
            setContent {
                UserVerification(
                    responseData = ResponseData.Loading
                )
            }

            onNodeWithTag(UserVerificationTag.LoadingIndicatorTag.toString())
                .assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun setResponseDataToError_showSnackBarWithMessage() {
        with(composeTestRule) {
            setContent {
                UserVerification(
                    responseData = ResponseData.Error("error")
                )
            }

            onNodeWithText("error")
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun setResendOTPResponseDataToError_showSnackBarWithMessage() {
        with(composeTestRule) {
            setContent {
                UserVerification(
                    resendOTPResponse = ResponseData.Error("otp resend error")
                )
            }

            onNodeWithText("otp resend error")
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun setResendOTPResponseDataToSuccess_emailIDIsNull_showSnackBarWithMessage() {
        with(composeTestRule) {
            setContent {
                UserVerification(
                    resendOTPResponse = ResponseData.Success(null)
                )
            }

            onNodeWithText("Failed to send OTP")
                .assertExists()
                .assertIsDisplayed()
        }
    }
}