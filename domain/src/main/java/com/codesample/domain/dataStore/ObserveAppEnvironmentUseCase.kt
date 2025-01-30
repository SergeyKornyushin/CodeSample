package com.codesample.domain.dataStore

import com.codesample.domain.LocalFlowUseCase
import com.codesample.domain.dataStore.model.AppEnvironment
import javax.inject.Inject

interface ObserveAppEnvironmentUseCase : LocalFlowUseCase<Unit, AppEnvironment>

class ObserveAppEnvironmentUseCaseImpl @Inject constructor(
    private val dataStore: DataStoreSource
) : ObserveAppEnvironmentUseCase {
    override fun execute(param: Unit) = dataStore.appEnvironment
}