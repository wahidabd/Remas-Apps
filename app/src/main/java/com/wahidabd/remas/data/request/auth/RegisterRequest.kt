package com.wahidabd.remas.data.request.auth

data class RegisterRequest(
    var id: String? = null,
    val name: String,
    val email: String,
    val password: String
)