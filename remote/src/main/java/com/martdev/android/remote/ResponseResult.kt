package com.martdev.android.remote

import com.martdev.android.domain.Result
import retrofit2.Response
import timber.log.Timber

open class ResponseResult {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body)
            }
            return Result.error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return Result.error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        Timber.e(message)
        return Result.error("Network call has failed for a following reason: $message")
    }

}

