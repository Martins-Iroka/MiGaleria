package com.martdev.domain.login

data class UserLoginDataRequest(
    val email: String,
    val password: String
)

data class UserLoginLogoutResponse(
    val isSuccessful: Boolean,
    val error: String
)
