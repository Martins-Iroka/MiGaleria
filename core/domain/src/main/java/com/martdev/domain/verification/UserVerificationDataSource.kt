package com.martdev.domain.verification

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

interface UserVerificationDataSource {

    fun verifyUser(user: UserVerificationDataRequest): Flow<ResponseData<Nothing>>
}