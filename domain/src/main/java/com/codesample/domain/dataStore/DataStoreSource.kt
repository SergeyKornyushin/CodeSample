package com.codesample.domain.dataStore

import com.codesample.domain.dataStore.model.AppEnvironment
import kotlinx.coroutines.flow.Flow

interface DataStoreSource {

    val isFirstLaunch: Flow<Boolean>
    suspend fun updateIsFirstLaunch(isFirstLaunch: Boolean)

    val appEnvironment: Flow<AppEnvironment>
    suspend fun updateAppEnvironment(environment: AppEnvironment)

    suspend fun clear()
}