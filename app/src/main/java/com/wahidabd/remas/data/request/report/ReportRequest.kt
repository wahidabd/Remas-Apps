package com.wahidabd.remas.data.request.report

import com.google.firebase.database.Exclude
import java.io.File

data class ReportRequest(
    var id: String? = null,
    val user_id: String? = null,
    val user_name: String? = null,
    val user_image: String? = null,
    val title: String? = null,
    val body: String? = null,
    var file: File? = null,
    var file_url: String? = null,
    var file_name: String? = null
) {

    @Exclude
    fun toMap(): Map<String, Any?> =
        mapOf(
            "id" to id,
            "title" to title,
            "body" to body,
            "file" to file_url,
            "file_name" to file_name
        )

    @Exclude
    fun toUserMap(): Map<String, Any?> =
        mapOf(
            "id" to user_id,
            "name" to user_name,
            "image" to user_image
        )

}
