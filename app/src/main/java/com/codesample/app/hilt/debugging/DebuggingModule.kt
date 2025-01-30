package com.codesample.app.hilt.debugging

import android.content.Context
import com.codesample.data.debugging.LaunchChuckerUseCaseImpl
import com.codesample.domain.debugging.LaunchChuckerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DebuggingModule {
    @Provides
    fun bindLaunchChuckerUseCase(
        @ApplicationContext context: Context
    ): LaunchChuckerUseCase = LaunchChuckerUseCaseImpl(context)
}