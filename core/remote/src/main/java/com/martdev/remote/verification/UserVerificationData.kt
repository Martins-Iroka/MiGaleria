package com.martdev.remote.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationRequestPayload(
    private val code: String,
    @SerialName("email_Id")
    private val emailID: String,
    private val token: String
)

@Serializable
data class UserVerificationResponsePayload(
    val status: String
)
