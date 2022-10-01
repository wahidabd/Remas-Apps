package com.wahidabd.remas.domain.usecase

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.models.User
import com.wahidabd.remas.data.request.auth.LoginRequest
import com.wahidabd.remas.data.request.auth.RegisterRequest
import com.wahidabd.remas.data.response.GenericResponse
import com.wahidabd.remas.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repo: UserRepository
) {

    suspend fun login(request: LoginRequest): Flow<Response<User>> = repo.login(request)

    suspend fun register(request: RegisterRequest): Flow<Response<GenericResponse>> =
        repo.register(request)

    suspend fun userDetail(id: String): Flow<Response<User>> = repo.userDetail(id)

}