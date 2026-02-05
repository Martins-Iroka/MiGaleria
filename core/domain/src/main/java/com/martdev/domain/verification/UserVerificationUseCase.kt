package com.martdev.domain.verification

class UserVerificationUseCase(
    val userVerificationDataSource: UserVerificationDataSource
) {

    operator fun invoke(code: String, emailID: String) =
        userVerificationDataSource.verifyUser(UserVerificationDataRequest(code, emailID))
}