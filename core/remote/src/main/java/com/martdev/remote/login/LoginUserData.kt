package com.martdev.remote.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginUserRequestPayload(
    private val email: String,
    private val password: String
)

@Serializable
data class LoginUserResponsePayload(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)
