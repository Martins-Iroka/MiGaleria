package com.martdev.remote.registration

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationRequestPayload(
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

@Serializable
data class UserRegistrationResponsePayload(
    val token: String = ""
)
