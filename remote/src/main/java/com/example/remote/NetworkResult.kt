package com.example.remote

import java.io.Serializable
import kotlin.Error

sealed class NetworkResult<out T> {

    object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T) : NetworkResult<T>()

    data class Error(
        val exception: Exception? = null,
        val error: String?,
        val code: Int = 0
    ) : NetworkResult<Nothing>(), Serializable
}

fun <T> NetworkResult<T>.successOr(fallback: T): T {
    return (this as? NetworkResult.Success<T>)?.data ?: fallback
}

val <T> NetworkResult<T>.data: T?
    get() = (this as? NetworkResult.Success)?.data

val <T> NetworkResult<T>.error: NetworkResult.Error?
    get() = (this as? NetworkResult.Error)

val <T> NetworkResult<T>.isSuccessful: Boolean
    get() = this is NetworkResult.Success && this !is Error

