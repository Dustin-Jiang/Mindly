package top.tsukino.llmdemo.feature.sharereceiver

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity

interface CollectionItemDisplay<T> {
    @Composable
    fun Display(): Unit

    fun toEntity() : T
}