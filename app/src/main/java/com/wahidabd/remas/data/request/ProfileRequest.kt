package com.wahidabd.remas.data.request

import com.google.firebase.database.Exclude
import java.io.File

data class ProfileRequest(
    val id: String,
    val file: File? = null,
    var file_url: String? = null,
    val name: String,
    val email: String,
    val age: Int? = null,
    val address: String? = null,
    val phone: String? = null
){

    @Exclude
    fun toMap(): Map<String, Any?> =
        mapOf(
            "id" to id,
            "image" to file_url,
            "name" to name,
            "email" to email,
            "age" to age,
            "address" to address,
            "phone" to address
        )

}
