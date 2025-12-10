package com.martdev.remote.datastore.user

import kotlinx.coroutines.flow.Flow

interface UserStorage {

    fun getUserData(): Flow<UserData>

    suspend fun saveUserId(userID: Long)

    suspend fun clearUserData()
}