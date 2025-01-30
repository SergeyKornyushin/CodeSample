package com.codesample.domain.auth

import com.codesample.domain.auth.model.Session
import com.codesample.domain.LocalFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveSessionUseCase : LocalFlowUseCase<Unit, Session>

class ObserveSessionUseCaseImpl @Inject constructor(
    private val dataSource: AuthDbDataSource,
) : ObserveSessionUseCase {

    override fun execute(param: Unit): Flow<Session> = dataSource.observeSession()
}
