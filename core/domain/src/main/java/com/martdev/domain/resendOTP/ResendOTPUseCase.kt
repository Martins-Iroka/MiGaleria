package com.martdev.domain.resendOTP

class ResendOTPUseCase(
    val resendOTPDataSource: ResendOTPDataSource
) {
    operator fun invoke(email: String) = resendOTPDataSource.resendOTP(email)
}