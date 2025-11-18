package com.martdev.remote.datastore

import kotlinx.coroutines.flow.Flow

interface TokenStorage {

    fun getTokens(): Flow<AuthToken>

    suspend fun saveTokens(token: AuthToken)

    suspend fun clearTokens()
}