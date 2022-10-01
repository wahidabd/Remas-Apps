package com.wahidabd.remas.di

import android.content.Context
import com.wahidabd.remas.data.repository.UserRepositoryImpl
import com.wahidabd.remas.data.storage.UserStorage
import com.wahidabd.remas.data.storage.firebase.FirebaseUserStorage
import com.wahidabd.remas.domain.repository.UserRepository
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
    fun provideMySharedPreferences(@ApplicationContext context: Context) =
        MySharedPreferences(context)

    @Provides
    @Singleton
    fun provideUserStorage() = FirebaseUserStorage()

    @Provides
    @Singleton
    fun provideUserRepository(storage: UserStorage): UserRepository =
        UserRepositoryImpl(storage)
}