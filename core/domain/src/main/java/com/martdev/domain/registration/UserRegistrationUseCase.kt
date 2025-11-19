package com.martdev.domain.registration

class UserRegistrationUseCase(
    private val userRegistrationDataSource: UserRegistrationDataSource
) {
    fun registerUser(email: String, password: String, username: String) =
        userRegistrationDataSource.registerUser(UserRegistrationDataRequest(
            email, username, password
        ))
}