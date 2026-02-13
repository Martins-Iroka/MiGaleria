package com.martdev.remote.registration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationRequestPayload(
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

@Serializable
data class UserRegistrationResponsePayload(
    @SerialName("email_id")
    val emailId: String = "",
    val token: String = ""
)
