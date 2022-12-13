package com.wahidabd.remas.domain.usecase

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportUserCase @Inject constructor(private val repo: ReportRepository) {

    suspend fun getUser(): Flow<Response<List<User>>> =
        repo.getUser()

}