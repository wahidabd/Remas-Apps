package com.wahidabd.remas.data.storage

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import kotlinx.coroutines.flow.Flow

interface ChatStorage {

    suspend fun getGroupChat(id: String): Flow<Response<List<UserChatGroupResponse>>>
    suspend fun sendMessage(request: ChatRoomRequest): Flow<Response<Boolean>>

}