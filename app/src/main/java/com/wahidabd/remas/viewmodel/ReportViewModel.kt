package com.wahidabd.remas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.report.ReportRequest
import com.wahidabd.remas.data.response.ReportDocumentResponse
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.domain.usecase.ReportUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val useCase: ReportUserCase) : ViewModel() {

    suspend fun createReport(request: ReportRequest): LiveData<Response<Boolean>> =
        useCase.createReport(request).asLiveData()

    suspend fun getUser(): LiveData<Response<List<User>>> =
        useCase.getUser().asLiveData()

    suspend fun getUserDocument(): LiveData<Response<List<User>>> =
        useCase.getUserDocument().asLiveData()

    suspend fun getReportByUser(id: String): LiveData<Response<List<ReportDocumentResponse>>> =
        useCase.getReportByUser(id).asLiveData()

}