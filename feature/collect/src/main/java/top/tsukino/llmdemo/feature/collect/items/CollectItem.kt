package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.runtime.Composable

interface CollectItem {
    val timestamp: Long

    @Composable
    fun Display(): Unit
}
