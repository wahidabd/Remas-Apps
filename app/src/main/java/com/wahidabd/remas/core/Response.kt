package com.wahidabd.remas.core

sealed class Response<out T> {
    class Loading<out T> : Response<T>()
    data class Success<out T>(val data: T): Response<T>()
    data class Error<out T>(val e: Exception) : Response<T>()
}