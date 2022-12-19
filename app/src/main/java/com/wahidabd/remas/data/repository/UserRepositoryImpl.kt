package com.wahidabd.remas.data.repository

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.ProfileRequest
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.data.request.auth.LoginRequest
import com.wahidabd.remas.data.request.auth.RegisterRequest
import com.wahidabd.remas.data.response.GenericResponse
import com.wahidabd.remas.data.storage.UserStorage
import com.wahidabd.remas.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage: UserStorage
) : UserRepository {

    override suspend fun login(request: LoginRequest): Flow<Response<User>> =
        storage.login(request)

    override suspend fun register(request: RegisterRequest): Flow<Response<GenericResponse>> =
        storage.register(request)

    override suspend fun resetPassword(email: String): Flow<Response<GenericResponse>> =
        storage.resetPassword(email)

    override suspend fun userDetail(id: String): Flow<Response<User>> =
        storage.userDetail(id)

    override suspend fun userList(id: String): Flow<Response<ArrayList<User>>> =
        storage.userList(id)

    override suspend fun editProfile(request: ProfileRequest): Flow<Response<User>> =
        storage.editProfile(request)

}