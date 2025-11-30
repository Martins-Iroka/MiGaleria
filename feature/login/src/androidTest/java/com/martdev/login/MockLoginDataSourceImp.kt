package com.martdev.login

import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginDataRequest
import com.martdev.domain.login.UserLoginDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockLoginDataSourceImp : UserLoginDataSource {
    override fun loginUser(user: UserLoginDataRequest): Flow<ResponseData<Nothing>> {
        return flowOf(ResponseData.Loading)
    }

    override fun logoutUser(): Flow<ResponseData<Unit>> {
        return flowOf(ResponseData.Loading)
    }
}