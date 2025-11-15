package com.martdev.remote.registration

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequestPayload(
    private val username: String,
    private val email: String,
    private val password: String
)

@Serializable
data class RegisterUserResponsePayload(
    private val token: String
)
