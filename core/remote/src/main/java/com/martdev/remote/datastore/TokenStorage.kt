package com.martdev.remote.datastore

import kotlinx.coroutines.flow.Flow

interface TokenStorage {

    fun getTokens(): Flow<AuthToken>

    suspend fun saveAuthTokens(token: AuthToken)

    suspend fun saveVerificationToken(token: String)

    suspend fun saveUserId(userID: Long)
    suspend fun clearTokens()
}