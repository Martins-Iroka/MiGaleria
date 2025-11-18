package com.martdev.remote.registration

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequestPayload(
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

@Serializable
data class RegisterUserResponsePayload(
    val token: String = ""
)
