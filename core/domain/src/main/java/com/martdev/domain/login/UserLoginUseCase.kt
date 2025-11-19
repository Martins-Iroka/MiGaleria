package com.martdev.domain.login

class UserLoginUseCase(
    private val userLoginDataSource: UserLoginDataSource
) {
    fun loginUser(email: String, password: String) =
        userLoginDataSource.loginUser(UserLoginDataRequest(email, password))
}