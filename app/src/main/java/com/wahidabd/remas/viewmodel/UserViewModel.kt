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

    private val _user: MutableLiveData<Response<User>> = MutableLiveData()
    val user: LiveData<Response<User>> get() = _user

    suspend fun login(request: LoginRequest): LiveData<Response<User>> =
        useCase.login(request).asLiveData()

    suspend fun register(request: RegisterRequest): LiveData<Response<GenericResponse>> =
        useCase.register(request).asLiveData()

    fun user(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.userDetail(id).collect {
                _user.postValue(it)
            }
        }
    }

}