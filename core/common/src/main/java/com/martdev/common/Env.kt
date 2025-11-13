package com.martdev.common

object Env {
    const val KEY_ALIAS = "KEY_ALIAS"
    fun getSecret(key: String): String? = System.getenv(key)
}