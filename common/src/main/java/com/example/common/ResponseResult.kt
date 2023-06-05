package com.example.common

sealed class ResponseResult<out T> {
    object Loading : ResponseResult<Nothing>()

    data class Success<out T>(
        val data: T
    ) : ResponseResult<T>()

    data class Failure(
        val e: Exception?
    ) : ResponseResult<Nothing>()

}