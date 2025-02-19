package com.codesample.domain.dataStore

import com.codesample.domain.BlockingUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface BlockingGetBaseUrlUseCase : BlockingUseCase<Unit, String>

class BlockingGetBaseUrlUseCaseImpl @Inject constructor(
    private val dataStore: DataStoreSource
) : BlockingGetBaseUrlUseCase {
    override suspend fun execute(param: Unit) = dataStore.appEnvironment.map { it.url }
}
