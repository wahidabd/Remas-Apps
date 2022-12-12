package com.wahidabd.remas.domain.repository

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getGroupChat(id: String): Flow<Response<List<UserChatGroupResponse>>>
    suspend fun sendMessage(request: ChatRoomRequest): Flow<Response<Boolean>>

}