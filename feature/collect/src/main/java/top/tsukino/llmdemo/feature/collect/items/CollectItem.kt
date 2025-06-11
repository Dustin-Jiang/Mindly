package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface CollectItem {
    val timestamp: Long
    val id: ItemId
    val category: Long?

    @Composable
    fun Display(modifier: Modifier): Unit
}
