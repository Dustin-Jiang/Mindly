package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.runtime.Composable

interface CollectItem {
    val timestamp: Long
    val id: ItemId
    val category: Long?

    @Composable
    fun Display(): Unit
}
