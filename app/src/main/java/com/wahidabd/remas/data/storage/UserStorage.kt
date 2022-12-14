package com.wahidabd.remas.data.storage

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.ProfileRequest
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.data.request.auth.LoginRequest
import com.wahidabd.remas.data.request.auth.RegisterRequest
import com.wahidabd.remas.data.response.GenericResponse
import kotlinx.coroutines.flow.Flow

interface UserStorage {

    suspend fun login(request: LoginRequest): Flow<Response<User>>
    suspend fun register(request: RegisterRequest): Flow<Response<GenericResponse>>
    suspend fun resetPassword(email: String): Flow<Response<GenericResponse>>
    suspend fun userDetail(id: String): Flow<Response<User>>
    suspend fun userList(id: String): Flow<Response<ArrayList<User>>>
    suspend fun editProfile(request: ProfileRequest): Flow<Response<User>>

}