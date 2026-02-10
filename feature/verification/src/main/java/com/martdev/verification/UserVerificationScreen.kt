package com.martdev.verification

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.martdev.domain.ResponseData
import com.martdev.ui.reusable.CustomLayout
import com.martdev.ui.reusable.MANROP_SEMI_BOLD
import com.martdev.ui.reusable.TextCompose
import com.martdev.ui.reusable.theme.Color_1F1F1F
import com.martdev.ui.reusable.theme.Color_4E0189
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.koin.androidx.compose.koinViewModel

sealed interface UserVerificationTag {
    data object UserVerificationScreenTag : UserVerificationTag
    data object CodeInputTag : UserVerificationTag
    data object LoadingIndicatorTag : UserVerificationTag
    data object VerifyButtonTag : UserVerificationTag
}

@Composable
fun UserVerificationScreen(
    emailID: String = "",
    email: String = "",
    navigate: () -> Unit = {}
) {
    val viewModel = koinViewModel<UserVerificationViewModel>()

    val response by viewModel.response.collectAsStateWithLifecycle()

    val resendOTPResponse by viewModel.resendOTPResponse.collectAsStateWithLifecycle()

    BackHandler {
        navigate()
    }

    UserVerification(
        emailID = emailID,
        responseData = response,
        resendOTPResponse,
        resendOTP = {
            viewModel.resendOTP(email)
        },
        resetOTP = {
            viewModel.resetResendOTPResponse()
        },
        verifyCode = { code, emailID ->
            viewModel.verifyCode(code, emailID)
        }
    )
}

@Composable
internal fun UserVerification(
    emailID: String = "",
    responseData: ResponseData<Nothing> = ResponseData.NoResponse,
    resendOTPResponse: ResponseData<String> = ResponseData.NoResponse,
    resendOTP: () -> Unit = {},
    resetOTP: () -> Unit = {},
    verifyCode: (String, String) -> Unit = {_, _ ->}
) {

    var eid by remember {
        mutableStateOf(emailID)
    }
    var timeLeft by remember { mutableLongStateOf(10000) }

    val seconds = (timeLeft / 1000) % 60

    var code by remember {
        mutableStateOf("")
    }
    var error by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            while (isActive && timeLeft > 0) { // Check isActive to ensure coroutine is still active
                delay(1000L) // Wait for 1 second
                timeLeft -= 1000L
            }
        }
    }

    LaunchedEffect(responseData) {
        if (responseData is ResponseData.Error){
            error = responseData.message
        }
    }

    LaunchedEffect(resendOTPResponse) {
        when (resendOTPResponse) {
            is ResponseData.Error -> {
                error = resendOTPResponse.message
            }
            is ResponseData.Success -> {
                resendOTPResponse.data?.let {
                    eid = it
                } ?: run {
                    error = "Failed to send OTP"
                    resetOTP()
                }
            }
            else -> { /* NoResponse or other states */ }
        }
    }

    CustomLayout(message = error) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(16.dp)
            .testTag(UserVerificationTag.UserVerificationScreenTag.toString())
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            TextCompose(
                "Verify Email",
                fontSize = 25,
                textColor = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextCompose("A code has been sent to your email", fontSize = 14, textColor = Color.Black)

            Spacer(modifier = Modifier.height(32.dp))

            TextCompose("Code", font = MANROP_SEMI_BOLD, fontSize = 14)

            OutlinedTextField(
                value = code,
                onValueChange = { c -> code = c },
                label = { TextCompose("Enter code",
                    textColor = Color_1F1F1F) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UserVerificationTag.CodeInputTag.toString()),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (responseData is ResponseData.Loading) {
                CircularProgressIndicator(
                    color = Color_4E0189,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .testTag(UserVerificationTag.LoadingIndicatorTag.toString())
                )
            } else {
                Button(
                    onClick = {
                        verifyCode(code, eid)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserVerificationTag.VerifyButtonTag.toString()),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color_4E0189)
                ) {
                    TextCompose("Verify", fontSize = 17, textColor = Color.White)
                }
            }

            if (seconds != 0L) {
                TextCompose("$seconds seconds",
                    textColor = Color.DarkGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp))
            }

            TextButton(onClick = {
                timeLeft = 10000L
                resendOTP()
            },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = seconds == 0L) {
                TextCompose("Resend OTP", textColor = if (seconds != 0L) Color.DarkGray else Color_4E0189)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserVerificationScreenPreview() = UserVerification()