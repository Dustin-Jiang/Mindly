package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.runtime.Composable

class TextItem(
    val data: String,
    val title: String,
    override val timestamp: Long,
    override val id: ItemId
) : CollectItem {
    @Composable
    override fun Display() {

    }
}