package com.martdev.remote.util

import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

var authToken = AuthToken()

class FakeTokenStorage : TokenStorage {
    override fun getTokens(): Flow<AuthToken> {
        return flowOf(authToken)
    }

    override suspend fun saveAuthTokens(token: AuthToken) {
        authToken = authToken.copy(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken
        )
    }

    override suspend fun saveVerificationToken(token: String) {
        authToken = authToken.copy(
            verificationToken = token
        )
    }

    override suspend fun clearTokens() {
        authToken
    }

}