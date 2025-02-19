package com.codesample.data.net.common.model

sealed class HeaderFlags {
    object NoToken : HeaderFlags() {
        const val key = "No-Tokens"
        const val value = "true"
        const val formattedHeader = "$key:$value"
    }
}
