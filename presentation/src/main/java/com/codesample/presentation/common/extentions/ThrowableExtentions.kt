package com.codesample.presentation.common.extentions

fun Throwable.logToFirebase(customKey: String? = null) {
    // todo обернуть в абстракцию в данном слое
//    Firebase.crashlytics.log(getFormattedThrowable(customKey = customKey, throwable = this))
//    kotlin.runCatching { }
}

fun getFormattedThrowable(customKey: String?, throwable: Throwable): String {
    return """
        ${if (customKey != null) "$customKey:" else ""}
        |message: ${throwable.message},
        |stackTrace: ${throwable.stackTraceToString()}.
    """.trimIndent()
}

fun <R> runCatchingLogToFirebase(block: () -> R): Result<R> = runCatching {
    block()
}.onFailure {
    it.logToFirebase()
}