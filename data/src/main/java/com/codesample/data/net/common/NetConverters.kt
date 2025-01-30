package com.codesample.data.net.common

import com.codesample.data.net.auth.model.AuthDataResponse
import com.codesample.data.net.common.model.ApiErrorResponse
import com.codesample.data.net.common.model.ApiSuccessResponse
import com.codesample.data.net.common.model.ErrorItemResponse
import com.codesample.domain.auth.model.AuthData
import com.codesample.domain.common.extentions.orDefault
import com.codesample.domain.common.model.ApiError
import com.codesample.domain.common.model.ApiSuccess
import com.codesample.domain.common.model.ErrorItem

@Suppress("TooManyFunctions", "LargeClass")
object NetConverters {

    fun AuthDataResponse.toDomain() = AuthData(
        accessToken = token.orEmpty(),
        refreshToken = refreshToken.orEmpty()
    )

    fun ApiErrorResponse?.toDomain() = ApiError(
        extraMessage = this?.extraMessage.orEmpty(),
        code = this?.code.orDefault(),
        message = this?.message.orEmpty(),
        violations = this?.violations?.map {
            it.toDomain()
        }.orEmpty(),
        key = this?.key.orEmpty(),
        email = this?.mail.orEmpty()
    )

    fun ErrorItemResponse?.toDomain() = ErrorItem(
        propertyPath = this?.propertyPath.orEmpty(),
        key = this?.key.orEmpty(),
        message = this?.message.orEmpty()
    )

    fun ApiSuccessResponse.toDomain() = ApiSuccess(
        message = message.orEmpty()
    )
}