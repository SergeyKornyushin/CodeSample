package com.codesample.domain.auth

import com.codesample.domain.auth.model.AuthData

interface AuthRemoteDataSource {
    suspend fun loginByPhone(email: String, password: String): AuthData
}