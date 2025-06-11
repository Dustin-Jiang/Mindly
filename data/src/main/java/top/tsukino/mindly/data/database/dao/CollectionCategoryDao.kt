package top.tsukino.mindly.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity

@Dao
interface CollectionCategoryDao {
    @Insert
    suspend fun insertCategory(category: CollectionCategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CollectionCategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CollectionCategoryEntity)

    @Query("SELECT * FROM collection_category WHERE id = :id")
    suspend fun getCategory(id: Long): CollectionCategoryEntity?

    @Query("SELECT * FROM collection_category")
    fun getCategoryList(): Flow<List<CollectionCategoryEntity>>
}