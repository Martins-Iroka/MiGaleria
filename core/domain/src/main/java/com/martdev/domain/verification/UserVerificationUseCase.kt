package com.martdev.domain.verification

class UserVerificationUseCase(
    val userVerificationDataSource: UserVerificationDataSource
) {

    operator fun invoke(code: String, email: String) =
        userVerificationDataSource.verifyUser(UserVerificationDataRequest(code, email))
}