package com.martdev.domain.registration

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

interface UserRegistrationDataSource {
    fun registerUser(user: UserRegistrationDataRequest): Flow<ResponseData<Nothing>>
}