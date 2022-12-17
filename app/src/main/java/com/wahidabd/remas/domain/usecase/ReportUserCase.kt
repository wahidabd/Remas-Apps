package com.wahidabd.remas.domain.usecase

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.report.ReportRequest
import com.wahidabd.remas.data.response.ReportDocumentResponse
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportUserCase @Inject constructor(private val repo: ReportRepository) {

    suspend fun createReport(request: ReportRequest): Flow<Response<Boolean>> =
        repo.createReport(request)

    suspend fun getUser(): Flow<Response<List<User>>> =
        repo.getUser()

    suspend fun getUserDocument(): Flow<Response<List<User>>> =
        repo.getUserDocument()

    suspend fun getReportByUser(id: String): Flow<Response<List<ReportDocumentResponse>>> =
        repo.getReportByUser(id)

}