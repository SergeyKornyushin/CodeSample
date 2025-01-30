package com.codesample.data.net.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiSuccessResponse(
    val message: String? = null
)