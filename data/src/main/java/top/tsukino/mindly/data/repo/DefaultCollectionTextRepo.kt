package top.tsukino.mindly.data.repo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.MindlyDatabase
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.data.repo.base.CollectionTextRepo
import javax.inject.Inject

class DefaultCollectionTextRepo @Inject constructor(
    private val database: MindlyDatabase,
    @ApplicationContext private val context: Context
): CollectionTextRepo {
    override suspend fun getCollectionText(
        id: Long
    ): CollectionTextEntity? {
        return database.collectionTextDao().getCollectionTextById(id)
    }

    override fun getCollectionTextList(): Flow<List<CollectionTextEntity>> {
        return database.collectionTextDao().getAllCollectionTexts()
    }

    override suspend fun insertCollectionText(
        collectionText: CollectionTextEntity
    ): Long {
        return database.collectionTextDao().insertCollectionText(collectionText)
    }

    override suspend fun deleteCollectionText(
        collectionText: CollectionTextEntity
    ) {
        return database.collectionTextDao().deleteCollectionText(collectionText)
    }

    override suspend fun updateCollectionText(
        collectionText: CollectionTextEntity
    ) {
        return database.collectionTextDao().updateCollectionText(collectionText)
    }

    override fun getCollectionTextsByCategory(
        categoryId: Long
    ): Flow<List<CollectionTextEntity>> {
        return database.collectionTextDao().getCollectionTextsByCategory(categoryId)
    }
}