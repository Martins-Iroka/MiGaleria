package com.martdev.domain.login

class UserLoginUseCase(
    private val userLoginDataSource: UserLoginDataSource
) {
    operator fun invoke(email: String, password: String) =
        userLoginDataSource.loginUser(UserLoginDataRequest(email, password))
}