package com.codesample.presentation.common.models

interface Identity<T : Any> {
    val id: T
    override fun equals(other: Any?): Boolean
}

typealias LongIdentity = Identity<Long>