package com.wahidabd.remas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import com.wahidabd.remas.domain.usecase.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val useCase: ChatUseCase) : ViewModel() {

    suspend fun getGroupChat(id: String): LiveData<Response<List<UserChatGroupResponse>>> =
        useCase.getGroupChat(id).asLiveData()

    suspend fun sendMessage(request: ChatRoomRequest): LiveData<Response<Boolean>> =
        useCase.sendMessage(request).asLiveData()

}