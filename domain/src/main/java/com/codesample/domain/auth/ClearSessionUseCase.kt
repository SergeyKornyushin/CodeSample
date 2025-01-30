package com.codesample.domain.auth

import com.codesample.domain.SuspendedUseCase
import javax.inject.Inject

interface ClearSessionUseCase : SuspendedUseCase<Unit, Unit>

class ClearSessionUseCaseImpl @Inject constructor(
    private val authDbDataSource: AuthDbDataSource,
) : ClearSessionUseCase {

    override suspend fun execute(param: Unit) {
        authDbDataSource.clearSessions()
    }
}
