package com.yusmp.basecode.presentation.common.extentions

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

fun Throwable.logToFirebase(customKey: String? = null) {
    Firebase.crashlytics.log(getFormattedThrowable(customKey = customKey, throwable = this))
}

fun getFormattedThrowable(customKey: String?, throwable: Throwable): String {
    return """
        ${if (customKey != null) "$customKey:" else ""}
        |message: ${throwable.message},
        |stackTrace: ${throwable.stackTraceToString()}.
    """.trimIndent()
}