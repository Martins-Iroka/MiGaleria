package com.martdev.android.remote

import com.martdev.android.domain.Result
import retrofit2.Response

open class ResponseResult {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            Result.loading(null)
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error("Network call has failed for a following reason: $message")
    }

}

