package com.codesample.domain.dataStore.model

enum class AppEnvironment(val url: String) {
    DEV("http://00.000.000.000:80"),
    UAT("http://11.111.11.111:80"),
}