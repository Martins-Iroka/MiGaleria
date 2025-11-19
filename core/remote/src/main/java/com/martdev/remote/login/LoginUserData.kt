package com.martdev.remote.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequestPayload(
    private val email: String,
    private val password: String
)

@Serializable
data class UserLoginResponsePayload(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)

@Serializable
data class LogoutUserRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)
