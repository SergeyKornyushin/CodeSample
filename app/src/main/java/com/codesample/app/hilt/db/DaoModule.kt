package com.codesample.app.hilt.db

import com.codesample.data.db.auth.SessionDao
import com.codesample.data.db.common.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun bindSessionDao(db: AppDatabase): SessionDao = db.sessionDao()
}