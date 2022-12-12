package com.wahidabd.remas.domain.usecase

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import com.wahidabd.remas.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatUseCase @Inject constructor(private val repo: ChatRepository){

    suspend fun getGroupChat(id: String): Flow<Response<List<UserChatGroupResponse>>> =
        repo.getGroupChat(id)

    suspend fun sendMessage(request: ChatRoomRequest): Flow<Response<Boolean>> =
        repo.sendMessage(request)

}