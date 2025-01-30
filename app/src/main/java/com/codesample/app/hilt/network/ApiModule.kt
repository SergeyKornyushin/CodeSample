package com.codesample.app.hilt.network

import com.codesample.data.net.auth.AuthApi
import com.codesample.data.net.common.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun bindAuthApi(retrofit: Retrofit): AuthApi = Network.getApi(retrofit)
}