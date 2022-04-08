package com.atanana.currencycompose

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.update(block: (T) -> T) {
    val newItems = map { block(it) }
    clear()
    addAll(newItems)
}