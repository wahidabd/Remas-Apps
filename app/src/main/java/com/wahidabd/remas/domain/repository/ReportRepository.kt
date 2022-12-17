package com.wahidabd.remas.domain.repository

import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.report.ReportRequest
import com.wahidabd.remas.data.response.ReportDocumentResponse
import com.wahidabd.remas.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    suspend fun createReport(request: ReportRequest): Flow<Response<Boolean>>
    suspend fun getUser(): Flow<Response<List<User>>>
    suspend fun getUserDocument(): Flow<Response<List<User>>>
    suspend fun getReportByUser(id: String): Flow<Response<List<ReportDocumentResponse>>>

}