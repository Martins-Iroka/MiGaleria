package com.martdev.remote.util

import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenStorage
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

var authToken = AuthToken()

class FakeTokenStorage : TokenStorage {
    override fun getTokens(): Flow<AuthToken> {
        return flowOf(authToken)
    }

    override suspend fun saveTokens(token: AuthToken) {
        authToken = authToken.copy(token.accessToken, token.refreshToken)
    }

    override suspend fun clearTokens() {
        authToken
    }

}