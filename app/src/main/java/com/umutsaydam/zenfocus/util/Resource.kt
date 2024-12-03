package com.umutsaydam.zenfocus.util

sealed class Resource<T>(
    val data: T? = null,
    val status: String? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(status: String? = null, data: T? = null, message: String? = null) :
        Resource<T>(status = status, data = data, message = message)
    class Loading<T> : Resource<T>()
}
