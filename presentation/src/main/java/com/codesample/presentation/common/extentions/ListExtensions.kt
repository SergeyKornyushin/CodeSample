package com.codesample.presentation.common.extentions

import com.codesample.presentation.common.models.Identity

fun <T> List<T>.replace(
    newItem: T,
    appendIfNotFound: Boolean = true,
    predicate: (T) -> Boolean = { newItem.id == it.id }
): List<T> where T : Identity<*> {
    val index = indexOfFirst(predicate)
    return if (index == -1) {
        if (appendIfNotFound) {
            this + newItem
        } else {
            this
        }
    } else {
        toMutableList().apply { set(index, newItem) }
    }
}

/**
 * Возвращает список элементов уникальных для каждого списка
 */
fun <T> Collection<T>.symmetricDifference(other: Collection<T>): Set<T> {
    val left = this subtract other.toSet()
    val right = other subtract this.toSet()
    return left union right
}