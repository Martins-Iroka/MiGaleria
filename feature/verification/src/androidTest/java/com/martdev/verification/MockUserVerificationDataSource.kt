package com.martdev.verification

import com.martdev.domain.ResponseData
import com.martdev.domain.verification.UserVerificationDataRequest
import com.martdev.domain.verification.UserVerificationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockUserVerificationDataSource : UserVerificationDataSource {
    override fun verifyUser(user: UserVerificationDataRequest): Flow<ResponseData<Nothing>> {
        return flowOf(ResponseData.Loading)
    }
}