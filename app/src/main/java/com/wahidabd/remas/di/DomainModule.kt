package com.wahidabd.remas.di

import com.wahidabd.remas.domain.repository.ChatRepository
import com.wahidabd.remas.domain.repository.ReportRepository
import com.wahidabd.remas.domain.repository.UserRepository
import com.wahidabd.remas.domain.usecase.ChatUseCase
import com.wahidabd.remas.domain.usecase.ReportUserCase
import com.wahidabd.remas.domain.usecase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideUserUseCase(repo: UserRepository) = UserUseCase(repo)

    @Provides
    @Singleton
    fun provideChatUseCase(repo: ChatRepository) = ChatUseCase(repo)

    @Provides
    @Singleton
    fun provideReportUseCase(repo: ReportRepository) = ReportUserCase(repo)
}