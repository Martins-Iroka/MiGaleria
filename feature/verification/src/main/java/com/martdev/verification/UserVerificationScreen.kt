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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import org.koin.androidx.compose.koinViewModel

sealed interface UserVerificationTag {
    data object UserVerificationScreenTag : UserVerificationTag
    data object CodeInputTag : UserVerificationTag
    data object LoadingIndicatorTag : UserVerificationTag
    data object VerifyButtonTag : UserVerificationTag
}

@Composable
fun UserVerificationScreen(
    email: String = "",
    navigate: () -> Unit = {}
) {
    val viewModel = koinViewModel<UserVerificationViewModel>()

    val response by viewModel.response.collectAsStateWithLifecycle()

    BackHandler {
        navigate()
    }

    UserVerification(
        responseData = response,
        verifyCode = {
            viewModel.verifyCode(it, email)
        }
    )
}

@Composable
internal fun UserVerification(
    responseData: ResponseData<Nothing> = ResponseData.NoResponse,
    verifyCode: (String) -> Unit = {}
) {

    var code by remember {
        mutableStateOf("")
    }
    var error by remember {
        mutableStateOf("")
    }

    LaunchedEffect(responseData) {
        if (responseData is ResponseData.Error){
            error = responseData.message
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
                        verifyCode(code)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserVerificationScreenPreview() = UserVerification()