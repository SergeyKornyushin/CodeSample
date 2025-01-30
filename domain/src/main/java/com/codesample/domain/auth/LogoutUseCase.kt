package com.codesample.domain.auth

import com.codesample.domain.SuspendedUseCase
import com.codesample.domain.dataStore.DataStoreSource
import javax.inject.Inject

interface LogoutUseCase : SuspendedUseCase<Unit, Unit>

@Suppress("LongParameterList")
class LogoutUseCaseImpl @Inject constructor(
    private val dataStore: DataStoreSource,
    private val authDbDataSource: AuthDbDataSource
) : LogoutUseCase {

    override suspend fun execute(param: Unit) {
        dataStore.clear()
        authDbDataSource.clearSessions()
    }
}
