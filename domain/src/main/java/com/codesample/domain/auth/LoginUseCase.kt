package com.codesample.domain.auth

import com.codesample.domain.auth.model.AuthData
import com.codesample.domain.auth.model.Session
import com.codesample.domain.FlowUseCase
import com.codesample.domain.auth.model.SessionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface LoginUseCase : FlowUseCase<LoginParams, AuthData>

class LoginUseCaseImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val dataSource: AuthDbDataSource,
) : LoginUseCase {
    override fun execute(param: LoginParams): Flow<AuthData> = flow {
        val authData: AuthData = authRemoteDataSource.loginByPhone(param.phone, "")
        Session(
            phone = param.phone,
            accessToken = authData.accessToken,
            refreshToken = authData.refreshToken,
            type = SessionType.AUTHORIZED_USER
        ).also { session ->
            dataSource.saveSession(session)
        }
        emit(authData)
    }
}

data class LoginParams(
    val phone: String,
)
