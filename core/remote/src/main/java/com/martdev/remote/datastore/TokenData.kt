package com.martdev.remote.datastore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AuthToken(
    val accessToken: String = "",
    val refreshToken: String = "",
    val verificationToken: String = ""
)

@Serializable
data class TokenRefreshRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)

@Serializable
data class TokenRefreshResponse(
    @SerialName("access_token")
    val accessToken: String
)
