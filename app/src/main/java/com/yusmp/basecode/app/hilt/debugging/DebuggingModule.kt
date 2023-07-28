package com.yusmp.basecode.app.hilt.debugging

import com.yusmp.data.debugging.LaunchChuckerUseCaseImpl
import com.yusmp.domain.debugging.LaunchChuckerUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DebuggingModule {
    @Binds
    fun bindLaunchChuckerUseCase(impl: LaunchChuckerUseCaseImpl): LaunchChuckerUseCase
}