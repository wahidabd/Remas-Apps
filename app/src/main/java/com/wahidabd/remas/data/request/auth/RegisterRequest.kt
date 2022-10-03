package com.wahidabd.remas.data.request.auth

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: Int = 1
)