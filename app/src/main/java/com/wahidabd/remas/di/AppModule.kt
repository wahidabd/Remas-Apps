package com.wahidabd.remas.di

import android.content.Context
import com.wahidabd.remas.data.repository.ChatRepositoryImpl
import com.wahidabd.remas.data.repository.ReportRepositoryImpl
import com.wahidabd.remas.data.repository.UserRepositoryImpl
import com.wahidabd.remas.data.storage.ChatStorage
import com.wahidabd.remas.data.storage.ReportStorage
import com.wahidabd.remas.data.storage.UserStorage
import com.wahidabd.remas.data.storage.firebase.FirebaseChatStorage
import com.wahidabd.remas.data.storage.firebase.FirebaseReportStorage
import com.wahidabd.remas.data.storage.firebase.FirebaseUserStorage
import com.wahidabd.remas.domain.repository.ChatRepository
import com.wahidabd.remas.domain.repository.ReportRepository
import com.wahidabd.remas.domain.repository.UserRepository
import com.wahidabd.remas.utils.Loading
import com.wahidabd.remas.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoading() = Loading()

    @Provides
    @Singleton
    fun provideMySharedPreferences(@ApplicationContext context: Context) =
        MySharedPreferences(context)

    @Provides
    @Singleton
    fun provideUserStorage(): UserStorage = FirebaseUserStorage()

    @Provides
    @Singleton
    fun provideUserRepository(storage: UserStorage): UserRepository =
        UserRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideChatStorage(): ChatStorage = FirebaseChatStorage()

    @Provides
    @Singleton
    fun provideChatRepository(storage: ChatStorage): ChatRepository =
        ChatRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideReportStorage(): ReportStorage = FirebaseReportStorage()

    @Provides
    @Singleton
    fun provideReportRepository(storage: ReportStorage): ReportRepository =
        ReportRepositoryImpl(storage)
}