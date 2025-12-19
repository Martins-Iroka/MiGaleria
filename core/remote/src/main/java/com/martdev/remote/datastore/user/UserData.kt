package com.martdev.remote.datastore.user

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: Long = 0
)