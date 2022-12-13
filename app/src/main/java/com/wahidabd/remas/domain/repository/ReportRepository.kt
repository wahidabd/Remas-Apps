package com.wahidabd.remas.domain.repository

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    suspend fun getUser(): Flow<Response<List<User>>>

}