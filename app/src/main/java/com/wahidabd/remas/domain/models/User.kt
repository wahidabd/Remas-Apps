package com.wahidabd.remas.domain.models

import com.wahidabd.remas.utils.Constants

data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val image: String? = Constants.STATIC_IMAGE,
    val role: Int? = null,
)