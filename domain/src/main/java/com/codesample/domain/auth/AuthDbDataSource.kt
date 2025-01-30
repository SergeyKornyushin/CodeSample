package com.codesample.domain.auth

import com.codesample.domain.auth.model.Session
import kotlinx.coroutines.flow.Flow

interface AuthDbDataSource {
    fun observeSession(): Flow<Session>
    fun getSession(): Session
    fun saveSession(session: Session)
    fun clearSessions()
}
