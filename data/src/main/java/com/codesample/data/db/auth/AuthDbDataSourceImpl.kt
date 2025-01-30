package com.codesample.data.db.auth

import com.codesample.data.db.common.DbConverters.toDomain
import com.codesample.data.db.common.DbConverters.toEntity
import com.codesample.data.net.common.Mock
import com.codesample.domain.auth.AuthDbDataSource
import com.codesample.domain.auth.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDbDataSourceImpl @Inject constructor(
    @Mock private val dao: SessionDao
) : AuthDbDataSource {

    override fun observeSession(): Flow<Session> =
        dao.observeLastSession().map { it.toDomain() }

    override fun getSession(): Session {
        return dao.getLastSession().toDomain()
    }

    override fun saveSession(session: Session) {
        dao.createSession(session.toEntity())
    }

    override fun clearSessions() {
        dao.clearSessions()
    }
}
