package com.codesample.app.hilt.mock

import com.codesample.data.db.auth.SessionDao
import com.codesample.data.mockdata.AuthApiMockImpl
import com.codesample.data.mockdata.SessionDaoMockImpl
import com.codesample.data.net.auth.AuthApi
import com.codesample.data.net.common.Mock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface MockDataModule {

    @Binds
    @Mock
    @Singleton
    fun bindMockAuthApi(impl: AuthApiMockImpl): AuthApi

    @Binds
    @Mock
    @Singleton
    fun bindMockAuthDao(impl: SessionDaoMockImpl): SessionDao
}
