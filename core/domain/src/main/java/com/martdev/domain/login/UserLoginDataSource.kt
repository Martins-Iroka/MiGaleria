package com.martdev.domain.login

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

interface UserLoginDataSource {

    fun loginUser(user: UserLoginDataRequest): Flow<ResponseData<Nothing>>

    fun logoutUser(): Flow<ResponseData<Unit>>
}