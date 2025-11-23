package com.martdev.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

sealed interface UserLoginTag {
    data object LoginScreenTag : UserLoginTag
    data object EnterEmailTag : UserLoginTag
    data object EnterPasswordTag : UserLoginTag
    data object LoginButtonTag : UserLoginTag
    data object ForgotPasswordButtonTag : UserLoginTag
    data object SignUpTextButtonTag : UserLoginTag
    data object LoginCircularTag : UserLoginTag
}

@Composable
fun UserLoginScreen(
    navigate: () -> Unit
) {

    val viewModel: UserLoginViewModel = koinViewModel()

    val response by viewModel.loginRes.collectAsStateWithLifecycle()

    UserLogin(
        responseData = response,
        loginUserClick = { email, password ->
            viewModel.loginUser(email = email, password = password)
        },
        signupClick = navigate
    )
}

@Composable
fun UserLogin(
    responseData: ResponseData<Nothing> = ResponseData.NoResponse,
    loginUserClick: (String, String) -> Unit = { _, _ -> },
    forgetPasswordClick: () -> Unit = {},
    signupClick: ()-> Unit= {}
) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    if (responseData is ResponseData.Error) {
        error = responseData.message
    }

    CustomLayout(message = error) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
                .testTag(UserLoginTag.LoginScreenTag.toString()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            TextCompose(
                "Hi, Welcome Back! \uD83D\uDC4B",
                font = MANROP_SEMI_BOLD,
                fontSize = 25,
                fontColor = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextCompose("Email", font = MANROP_SEMI_BOLD, fontSize = 14, fontColor = Color_4E0189)

            OutlinedTextField(
                value = email,
                onValueChange = { e -> email = e },
                label = { TextCompose("Enter email",
                    fontColor = Color_1F1F1F) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UserLoginTag.EnterEmailTag.toString()),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextCompose("Password", font = MANROP_SEMI_BOLD, fontSize = 14, fontColor = Color_4E0189)

            OutlinedTextField(
                value = password,
                onValueChange = { p -> password = p },
                label = { TextCompose("Enter Password",
                    fontColor = Color_1F1F1F) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UserLoginTag.EnterPasswordTag.toString()),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, "")
                    }
                },
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (responseData is ResponseData.Loading) {
                CircularProgressIndicator(
                    color = Color_4E0189,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .testTag(UserLoginTag.LoginCircularTag.toString())
                )
            } else {
                Button(
                    onClick = {
                        loginUserClick(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserLoginTag.LoginButtonTag.toString()),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color_4E0189)
                ) {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = forgetPasswordClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag(UserLoginTag.ForgotPasswordButtonTag.toString())
            ) {
                Text("Forgot Password")
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account?")
                TextButton(
                    onClick = signupClick,
                    modifier = Modifier
                        .testTag(UserLoginTag.SignUpTextButtonTag.toString())) {
                    Text("Sign Up")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserLoginScreenPreview() {
    UserLogin{}
}