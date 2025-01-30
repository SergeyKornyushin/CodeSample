package com.codesample.app.hilt.db

import android.content.Context
import com.codesample.data.db.common.AppDatabase
import com.codesample.data.db.common.Database
import com.codesample.data.db.common.DatabaseSupportFactoryProvider
import com.codesample.data.db.common.DbTransactionProcessorImpl
import com.codesample.data.db.common.DefaultDatabaseSupportFactoryProvider
import com.codesample.domain.common.DbTransactionProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseSupportFactoryProvider(
        @ApplicationContext context: Context
    ): DatabaseSupportFactoryProvider {
        return DefaultDatabaseSupportFactoryProvider(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        factory: DatabaseSupportFactoryProvider
    ): AppDatabase = Database.build(context = context, factory = factory)

    @Provides
    fun provideDbTransactionProcessor(
        database: AppDatabase
    ): DbTransactionProcessor = DbTransactionProcessorImpl(database)
}