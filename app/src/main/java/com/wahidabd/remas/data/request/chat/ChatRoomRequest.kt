package com.wahidabd.remas.data.request.chat

import com.google.firebase.database.Exclude

data class ChatRoomRequest(
    var id: String? = "",
    val sender_id: String,
    val receiver_id: String,
    val sender_name: String,
    val receiver_name: String,
    val sender_image: String,
    val receiver_image: String,
    val message: String,
    val date: String,
    val read: Boolean? = false,
    var unread: Int? = 0
) {

    @Exclude
    fun toMessage(): Map<String, Any?> =
        mapOf(
            "id" to id,
            "message" to message,
            "sender_id" to sender_id,
            "receiver_id" to receiver_id,
            "read" to read,
            "date" to date
        )

    fun toSender(): Map<String, Any?> =
        mapOf(
            "id" to sender_id,
            "name" to sender_name,
            "image" to sender_image,
            "message" to message,
            "date" to date,
            "unread" to unread
        )

    fun toReceiver(): Map<String, Any?> =
        mapOf(
            "id" to receiver_id,
            "name" to receiver_name,
            "image" to receiver_image,
            "message" to message,
            "date" to date,
            "unread" to unread
        )

}
