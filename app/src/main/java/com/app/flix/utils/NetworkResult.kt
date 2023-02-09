package com.app.flix.utils

/**
 * Used for handling the api(local/server) response
 * */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int = -1) {

    class Success<T>(data: T) : NetworkResult<T>(data)

    class Error<T>(message: String?, code: Int = -1, data: T? = null) : NetworkResult<T>(data, message, code)

    class Loading<T> : NetworkResult<T>()

}