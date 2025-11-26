package com.martdev.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
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
import com.martdev.ui.reusable.theme.Color_999EA1
import org.koin.androidx.compose.koinViewModel

sealed interface UserRegistrationTag {
    data object RegistrationScreen : UserRegistrationTag
    data object EmailInput : UserRegistrationTag
    data object PasswordInput : UserRegistrationTag
    data object UsernameInput : UserRegistrationTag
    data object SignUpButton : UserRegistrationTag
    data object LoginButton : UserRegistrationTag
    data object LoadingIndicator : UserRegistrationTag
}


@Composable
fun UserRegistrationComposable(
    navigate: () -> Unit
) {

    val viewModel: UserRegistrationViewModel = koinViewModel()

    val response by viewModel.response.collectAsStateWithLifecycle()

    UserRegistration(
        responseData = response,
        loginUserClicked = navigate,
        signUpUserClicked = {email, password, username ->
            viewModel.registerUser(email, password, username)
        }
    )
}

@Composable
internal fun UserRegistration(
    responseData: ResponseData<Nothing> = ResponseData.NoResponse,
    loginUserClicked: () -> Unit = {},
    signUpUserClicked: (String, String, String) -> Unit = { _, _, _ -> }
) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(responseData) {
        if (responseData is ResponseData.Error) {
            error = responseData.message
        }
    }

    CustomLayout(message = error) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .testTag(UserRegistrationTag.RegistrationScreen.toString())
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            TextCompose(
                "Create an account",
                font = MANROP_SEMI_BOLD,
                fontSize = 25,
                textColor = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextCompose("Email", font = MANROP_SEMI_BOLD, fontSize = 14, textColor = Color_4E0189)

            OutlinedTextField(
                value = email,
                onValueChange = { e -> email = e },
                label = { TextCompose("Enter email",
                    textColor = Color_1F1F1F) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UserRegistrationTag.EmailInput.toString()),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextCompose("Password", font = MANROP_SEMI_BOLD, fontSize = 14, textColor = Color_4E0189)

            OutlinedTextField(
                value = password,
                onValueChange = { p -> password = p },
                label = { TextCompose("Enter Password",
                    textColor = Color_1F1F1F) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UserRegistrationTag.PasswordInput.toString()),
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

            TextCompose("Username", font = MANROP_SEMI_BOLD, fontSize = 14, textColor = Color_4E0189)

            OutlinedTextField(
                value = username,
                onValueChange = { u -> username = u },
                label = { TextCompose("Enter username",
                    textColor = Color_1F1F1F) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UserRegistrationTag.UsernameInput.toString()),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (responseData is ResponseData.Loading) {
                CircularProgressIndicator(
                    color = Color_4E0189,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .testTag(UserRegistrationTag.LoadingIndicator.toString())
                )
            } else {
                Button(
                    onClick = {
                        signUpUserClicked(email, password, username)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserRegistrationTag.SignUpButton.toString()),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color_4E0189)
                ) {
                    TextCompose("Sign Up", fontSize = 17, textColor = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextCompose("Already have an account?", textColor = Color_999EA1)
                TextButton(
                    onClick = loginUserClicked,
                    modifier = Modifier
                        .testTag(UserRegistrationTag.LoginButton.toString())) {
                    TextCompose("Login")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun UserRegistrationScreenPreview() {
    UserRegistration()
}