package com.martdev.domain

sealed class ResponseData<out T> {
    data object Loading : ResponseData<Nothing>()
    data class Success<out T>(val data: T?) : ResponseData<T>()
    data class Error(val message: String) : ResponseData<Nothing>()
    data object NoResponse : ResponseData<Nothing>()
}