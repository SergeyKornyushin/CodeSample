package com.codesample.domain.common

interface DbTransactionProcessor {
    suspend fun runInTransaction(body: suspend () -> Unit)
}