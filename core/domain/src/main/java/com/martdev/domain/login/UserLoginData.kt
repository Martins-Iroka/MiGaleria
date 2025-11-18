package com.martdev.domain.login

data class UserLoginDataRequest(
    private val email: String,
    private val password: String
)

data class UserLoginLogoutResponse(
    val isSuccessful: Boolean,
    val error: String
)
