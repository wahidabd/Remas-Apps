package com.wahidabd.remas.data.response.chat

data class ChatRoomResponse(
    val id: String? = null,
    val message: String? = null,
    val sender_id: String? = null,
    val receiver_id: String? = null,
    val date: String? = null,
    val read: Boolean? = false
)
