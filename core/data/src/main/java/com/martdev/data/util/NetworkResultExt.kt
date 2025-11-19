@file:Suppress("UNCHECKED_CAST")

package com.martdev.data.util

import com.martdev.common.NetworkResult
import com.martdev.domain.ResponseData

inline fun <T : Any, R : Any> NetworkResult<T>.toResponseData(
    onSuccess: (T) -> R? = { it as? R }
): ResponseData<R> {
    return when (this) {
        is NetworkResult.Success -> ResponseData.Success(onSuccess(data))
        is NetworkResult.Failure -> ResponseData.Error(this.error)
    }
}