package com.codesample.data.db.common

import com.codesample.data.db.auth.SessionEntity
import com.codesample.data.db.auth.SessionTypeEntity
import com.codesample.domain.auth.model.Session
import com.codesample.domain.auth.model.SessionType

@Suppress("TooManyFunctions", "LargeClass")
object DbConverters {

    @Suppress("UseIfInsteadOfWhen")
    fun SessionEntity?.toDomain(): Session {
        this ?: return Session.EMPTY
        return Session(
            type = type.toDomain(),
            accessToken = accessToken,
            refreshToken = refreshToken,
            phone = phone,
        )
    }

    fun SessionType.toEntity() = when (this) {
        SessionType.NONE -> SessionTypeEntity.NONE
        SessionType.AUTHORIZED_USER -> SessionTypeEntity.AUTHORIZED_USER
    }

    fun SessionTypeEntity.toDomain() = when (this) {
        SessionTypeEntity.NONE -> SessionType.NONE
        SessionTypeEntity.AUTHORIZED_USER -> SessionType.AUTHORIZED_USER
    }

    fun Session.toEntity() = SessionEntity(
        type = type.toEntity(),
        accessToken = accessToken,
        refreshToken = refreshToken,
        phone = phone,
    )
}
