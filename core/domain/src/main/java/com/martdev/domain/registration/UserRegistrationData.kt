package com.martdev.domain.registration

data class UserRegistrationDataRequest(
    val email: String,
    val username: String,
    val password: String
)