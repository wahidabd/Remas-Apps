package com.wahidabd.remas.data.repository

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.storage.ReportStorage
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(private val storage: ReportStorage) : ReportRepository {

    override suspend fun getUser(): Flow<Response<List<User>>> =
        storage.getUser()

}