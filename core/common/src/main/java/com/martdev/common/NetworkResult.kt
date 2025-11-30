package com.martdev.common


sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    sealed class Failure(val error: String) : NetworkResult<Nothing> {
        data class BadRequest(val message: String = "Bad Request") : Failure(message)
        data class Unauthorized(val message: String = "Unauthorized") : Failure(message)
        data class NotFound(val message: String = "Not Found") : Failure(message)
        data class InternalServerError(val message: String = "Internal Server Error"): Failure(message)
        data class Other(val cause: Throwable) : Failure(cause.message.orEmpty().ifEmpty { "An error occurred" })
    }
}