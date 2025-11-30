package com.martdev.data.login

import com.martdev.data.util.toResponseData
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginDataRequest
import com.martdev.domain.login.UserLoginDataSource
import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.login.LogoutUserRequest
import com.martdev.remote.login.UserLoginRemoteSource
import com.martdev.remote.login.UserLoginRequestPayload
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

@OptIn(ExperimentalCoroutinesApi::class)
class UserLoginDataSourceImpl(
    private val remote: UserLoginRemoteSource,
    private val tokenStorage: TokenStorage
) : UserLoginDataSource {
    override fun loginUser(user: UserLoginDataRequest): Flow<ResponseData<Nothing>> {
        return remote.loginUser(UserLoginRequestPayload(user.email, user.password))
            .map {
                it.toResponseData { loginPayload ->
                    val accessToken = loginPayload.data.accessToken
                    val refreshToken = loginPayload.data.refreshToken
                    tokenStorage.saveAuthTokens(AuthToken(accessToken, refreshToken))
                    null
                }
            }
    }

    override fun logoutUser(): Flow<ResponseData<Unit>> {
        return flow {
            val token = tokenStorage.getTokens().firstOrNull()?: throw IllegalStateException("No refresh token found to log out.")
            val r =
                remote.logoutUser(LogoutUserRequest(refreshToken = token.refreshToken)).first()
            val responseData = r.toResponseData<Unit, Unit>()
            emit(responseData)
        }.onCompletion {
            tokenStorage.clearTokens()
        }
    }
}