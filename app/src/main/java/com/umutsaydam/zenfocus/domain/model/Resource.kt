package com.umutsaydam.zenfocus.domain.model

sealed class Resource<T>(
    val data: T? = null,
    val status: String? = null,
    val message: Int? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(status: String? = null, data: T? = null, message: Int? = null) :
        Resource<T>(status = status, data = data, message = message)
}
