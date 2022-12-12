package com.wahidabd.remas.data.repository

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import com.wahidabd.remas.data.storage.ChatStorage
import com.wahidabd.remas.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val storage: ChatStorage
) : ChatRepository {

    override suspend fun getGroupChat(id: String): Flow<Response<List<UserChatGroupResponse>>> =
        storage.getGroupChat(id)

    override suspend fun sendMessage(request: ChatRoomRequest): Flow<Response<Boolean>> =
        storage.sendMessage(request)
}