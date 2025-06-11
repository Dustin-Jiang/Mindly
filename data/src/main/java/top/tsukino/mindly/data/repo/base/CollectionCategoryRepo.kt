package top.tsukino.mindly.data.repo.base

import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity

interface CollectionCategoryRepo {
    suspend fun getCategory(id: Long): CollectionCategoryEntity?

    fun getCategoryList(): Flow<List<CollectionCategoryEntity>>

    suspend fun insertCategory(category: CollectionCategoryEntity): Long
    suspend fun deleteCategory(category: CollectionCategoryEntity)
    suspend fun updateCategory(category: CollectionCategoryEntity)
}