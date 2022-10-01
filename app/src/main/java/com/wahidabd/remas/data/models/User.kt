package com.wahidabd.remas.data.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profile: String? = null,
    val token: String,
    val image: String? = null,
    val role: Boolean
)