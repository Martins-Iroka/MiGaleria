package com.martdev.domain.registration

class UserRegistrationUseCase(
    val userRegistrationDataSource: UserRegistrationDataSource
) {
    operator fun invoke(email: String, password: String, username: String) =
        userRegistrationDataSource.registerUser(UserRegistrationDataRequest(
            email, username, password
        ))
}