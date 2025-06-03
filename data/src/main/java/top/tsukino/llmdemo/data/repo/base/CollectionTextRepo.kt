package top.tsukino.llmdemo.data.repo.base

import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity

interface CollectionTextRepo {
    suspend fun getCollectionText(id: Long) : CollectionTextEntity?

    fun getCollectionTextList(): Flow<List<CollectionTextEntity>>

    suspend fun insertCollectionText(recording: CollectionTextEntity): Long
    suspend fun deleteCollectionText(recording: CollectionTextEntity)
    suspend fun updateCollectionText(recording: CollectionTextEntity)
}