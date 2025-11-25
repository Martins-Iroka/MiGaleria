package com.martdev.domain.verification

class UserVerificationUseCase(
    val userVerificationDataSource: UserVerificationDataSource
) {

    fun verifyUser(code: String, email: String) =
        userVerificationDataSource.verifyUser(UserVerificationDataRequest(code, email))
}