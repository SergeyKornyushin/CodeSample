package com.codesample.domain.auth

import com.codesample.domain.auth.model.Session
import com.codesample.domain.SuspendedUseCase
import javax.inject.Inject

interface GetCurrentSessionUseCase : SuspendedUseCase<Unit, Session>

class GetCurrentSessionUseCaseImpl @Inject constructor(
    private val dataSource: AuthDbDataSource
) : GetCurrentSessionUseCase {

    override suspend fun execute(param: Unit): Session {
        return dataSource.getSession()
    }
}
