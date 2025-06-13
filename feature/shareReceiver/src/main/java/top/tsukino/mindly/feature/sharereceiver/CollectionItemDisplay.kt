package top.tsukino.mindly.feature.sharereceiver

import androidx.compose.runtime.Composable

interface CollectionItemDisplay<T> {
    @Composable
    fun Display(): Unit

    fun toEntity(
        category: Long?
    ) : T
}