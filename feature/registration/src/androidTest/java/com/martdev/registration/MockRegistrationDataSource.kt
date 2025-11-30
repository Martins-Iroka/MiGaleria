package com.martdev.registration

import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationDataRequest
import com.martdev.domain.registration.UserRegistrationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockRegistrationDataSource : UserRegistrationDataSource{
    override fun registerUser(user: UserRegistrationDataRequest): Flow<ResponseData<Nothing>> {
        return flowOf(ResponseData.Loading)
    }
}