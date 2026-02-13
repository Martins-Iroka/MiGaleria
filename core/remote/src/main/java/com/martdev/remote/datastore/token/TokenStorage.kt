package com.martdev.remote.datastore.token

import kotlinx.coroutines.flow.Flow

interface TokenStorage {

    fun getTokens(): Flow<AuthToken>

    suspend fun saveAuthTokens(token: AuthToken)

    suspend fun saveVerificationToken(token: String)

    suspend fun clearTokens()
}