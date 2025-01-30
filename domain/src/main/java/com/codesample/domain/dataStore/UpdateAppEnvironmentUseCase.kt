package com.codesample.domain.dataStore

import com.codesample.domain.SuspendedUseCase
import com.codesample.domain.dataStore.model.AppEnvironment
import javax.inject.Inject

interface UpdateAppEnvironmentUseCase : SuspendedUseCase<AppEnvironment, Unit>

class UpdateAppEnvironmentUseCaseImpl @Inject constructor(
    private val dataStore: DataStoreSource
) : UpdateAppEnvironmentUseCase {
    override suspend fun execute(param: AppEnvironment) =
        dataStore.updateAppEnvironment(param)
}