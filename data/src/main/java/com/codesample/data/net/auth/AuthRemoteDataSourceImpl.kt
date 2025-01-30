package com.codesample.data.net.auth

import com.codesample.data.net.auth.model.AuthRequestBody
import com.codesample.data.net.common.Mock
import com.codesample.data.net.common.NetConverters.toDomain
import com.codesample.domain.auth.AuthRemoteDataSource
import com.codesample.domain.auth.model.AuthData
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    @Mock private val authApi: AuthApi
) : AuthRemoteDataSource {
    override suspend fun loginByPhone(email: String, password: String): AuthData {
        return authApi.authByEmail(AuthRequestBody(phone = email)).toDomain()
    }
}