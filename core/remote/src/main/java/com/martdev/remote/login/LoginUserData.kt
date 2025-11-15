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
    private val accessToken: String,
    @SerialName("refresh_token")
    private val refreshToken: String
)
