package com.wahidabd.remas.viewmodel

import androidx.lifecycle.*
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.models.User
import com.wahidabd.remas.data.request.auth.LoginRequest
import com.wahidabd.remas.data.request.auth.RegisterRequest
import com.wahidabd.remas.data.response.GenericResponse
import com.wahidabd.remas.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UserUseCase
) : ViewModel() {

    suspend fun login(request: LoginRequest): LiveData<Response<User>> =
        useCase.login(request).asLiveData()

    suspend fun register(request: RegisterRequest): LiveData<Response<GenericResponse>> =
        useCase.register(request).asLiveData()

    suspend fun resetPassword(email: String): LiveData<Response<GenericResponse>> =
        useCase.resetPassword(email).asLiveData()

    suspend fun user(id: String): LiveData<Response<User>> =
        useCase.userDetail(id).asLiveData()

    suspend fun userList(): LiveData<Response<ArrayList<User>>> =
        useCase.userList().asLiveData()

}