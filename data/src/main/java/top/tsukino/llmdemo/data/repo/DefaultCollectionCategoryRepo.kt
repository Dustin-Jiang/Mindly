package top.tsukino.llmdemo.data.repo

import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.LLMDemoDatabase
import top.tsukino.llmdemo.data.database.entity.CollectionCategoryEntity
import top.tsukino.llmdemo.data.repo.base.CollectionCategoryRepo
import javax.inject.Inject

class DefaultCollectionCategoryRepo @Inject constructor(
    private val database: LLMDemoDatabase
) : CollectionCategoryRepo {
    override suspend fun getCategory(
        id: Long
    ): CollectionCategoryEntity? {
        return database.collectionCategoryDao().getCategory(id)
    }

    override fun getCategoryList(): Flow<List<CollectionCategoryEntity>> {
        return database.collectionCategoryDao().getCategoryList()
    }

    override suspend fun insertCategory(
        category: CollectionCategoryEntity
    ): Long {
        return database.collectionCategoryDao().insertCategory(category)
    }

    override suspend fun deleteCategory(
        category: CollectionCategoryEntity
    ) {
        return database.collectionCategoryDao().deleteCategory(category)
    }

    override suspend fun updateCategory(
        category: CollectionCategoryEntity
    ) {
        return database.collectionCategoryDao().updateCategory(category)
    }
}