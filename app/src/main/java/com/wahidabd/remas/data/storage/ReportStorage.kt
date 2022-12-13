package com.wahidabd.remas.data.storage

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ReportStorage {

    suspend fun getUser(): Flow<Response<List<User>>>

}