package com.codesample.data.mockdata

import com.codesample.data.net.auth.AuthApi
import com.codesample.data.net.auth.model.AuthDataResponse
import com.codesample.data.net.auth.model.AuthRequestBody
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class AuthApiMockImpl @Inject constructor() : AuthApi {
    override suspend fun authByEmail(body: AuthRequestBody): AuthDataResponse {
        delay(500)
        return when (Random.nextBoolean()) {
            true -> AuthDataResponse()
            false -> throw Exception("Something went wrong")
        }
    }
}