package com.codesample.app.hilt.dataSourcesUsecasesModules;

import android.content.Context
import com.codesample.data.local.datastore.DataStoreSourceImpl
import com.codesample.domain.dataStore.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    // region use cases
    @Binds
    fun bindBlockingGetBaseUrlUseCase(impl: BlockingGetBaseUrlUseCaseImpl): BlockingGetBaseUrlUseCase

    @Binds
    fun bindObserveAppEnvironmentUseCase(impl: ObserveAppEnvironmentUseCaseImpl): ObserveAppEnvironmentUseCase

    @Binds
    fun bindUpdateAppEnvironmentUseCase(impl: UpdateAppEnvironmentUseCaseImpl): UpdateAppEnvironmentUseCase

    @Binds
    fun bindGetIsFirstLaunchAndSetFalseUseCase(impl: GetIsFirstLaunchAndSetFalseUseCaseImpl): GetIsFirstLaunchAndSetFalseUseCase
    // endregion

    // region data sources
    companion object {
        @Provides
        fun provideDataStoreSource(
            @ApplicationContext context: Context
        ): DataStoreSource = DataStoreSourceImpl(context)
    }
    // endregion
}