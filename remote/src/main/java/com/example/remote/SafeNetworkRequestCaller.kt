package com.example.remote

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeNetworkRequestCaller {
    val dispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    val gson: Gson
}

suspend fun <T> SafeNetworkRequestCaller.request(
    apiCall: suspend () -> T
)
        : NetworkResult<T> = withContext(this.dispatcher) {
    try {
        NetworkResult.Success(apiCall.invoke())
    } catch (exception: Exception) {
        when (exception) {
            is HttpException -> {
                val throwableBodyString = exception.response()?.errorBody().toString()
                NetworkResult.Error(
                    exception,
                    throwableBodyString,
                    exception.response()?.code() ?: 0
                )
            }
            else -> {
                NetworkResult.Error(exception, null)
            }
        }
    }
}
