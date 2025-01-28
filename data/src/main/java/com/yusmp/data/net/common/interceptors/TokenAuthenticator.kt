package com.yusmp.data.net.common.interceptors

import com.yusmp.data.net.auth.model.AuthDataResponse
import com.yusmp.data.net.auth.model.RefreshTokenRequest
import com.yusmp.domain.auth.ClearSessionUseCase
import com.yusmp.domain.auth.GetCurrentSessionUseCase
import com.yusmp.domain.auth.UpdateTokensParams
import com.yusmp.domain.auth.UpdateTokensUseCase
import com.yusmp.domain.auth.model.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class TokenAuthenticator(
    private val appScope: CoroutineScope,
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase,
    private val updateTokensUseCase: UpdateTokensUseCase,
    private val clearSessionUseCase: ClearSessionUseCase,
    private val apiUrl: String,
    private val serializer: Json,
    private val isDebugEnvironment: Boolean,
) : Authenticator {

    private val mutex = Mutex()
    private var refreshTokenJob: Job? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        val session = runBlocking { getCurrentSessionUseCase(Unit) }

        if (session.isAuthorized().not() || session.refreshToken.isBlank()) return null

        runBlocking {
            mutex.withLock { updateToken(session = session) }

            refreshTokenJob?.join()
        }

        val updatedSession = runBlocking { getCurrentSessionUseCase(Unit) }
        return response.request.newBuilder()
            .header(
                name = AUTHORIZATION_HEADER_NAME,
                value = "$AUTHORIZATION_HEADER_PREFIX${updatedSession.accessToken}"
            )
            .build()
    }

    private fun updateToken(session: Session) {
        if (refreshTokenJob != null && refreshTokenJob?.isCompleted == false) return

        refreshTokenJob = appScope.launch {
            getRefreshToken(RefreshTokenRequest(session.refreshToken))?.let { token ->
                updateTokensUseCase(UpdateTokensParams(token, session.refreshToken))
            }
        }
    }

    private fun getRefreshToken(refreshToken: RefreshTokenRequest): String? {
        val requestBody = serializer.encodeToString(RefreshTokenRequest.serializer(), refreshToken)
            .toRequestBody(contentType = MEDIA_TYPE_VALUE.toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$apiUrl$REFRESH_PATH")
            .post(requestBody)
            .build()
        val response = createOkHttpClient().newCall(request).execute()

        return when (response.isSuccessful) {
            true -> runCatching {
                val stringBody = response.body?.string()
                when (stringBody.isNullOrBlank()) {
                    true -> null
                    else -> serializer.decodeFromString(
                        deserializer = AuthDataResponse.serializer(),
                        string = stringBody
                    ).token
                }
            }.getOrNull()

            false -> {
                appScope.launch { clearSessionUseCase(Unit) }
                null
            }
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            if (isDebugEnvironment) {
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(interceptor = this)
                }
            }
        }.build()
    }

    private companion object {
        const val REFRESH_PATH = "/tokenRefresh"
        const val CONNECT_TIMEOUT = 15L
        const val READ_TIMEOUT = 60L
        const val WRITE_TIMEOUT = 30L
        const val MEDIA_TYPE_VALUE = "application/json; charset=utf-8"
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
        const val AUTHORIZATION_HEADER_PREFIX = "Bearer "
    }
}