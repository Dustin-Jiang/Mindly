package top.tsukino.llmdemo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity

@Dao
interface CollectionTextDao {
    @Insert
    suspend fun insertCollectionText(recording: CollectionTextEntity): Long

    @Update
    suspend fun updateCollectionText(recording: CollectionTextEntity)

    @Delete
    suspend fun deleteCollectionText(recording: CollectionTextEntity)

    @Query("SELECT * FROM collection_text WHERE id = :id")
    suspend fun getCollectionTextById(id: Long): CollectionTextEntity?

    @Query("SELECT * FROM collection_text")
    fun getAllCollectionTexts(): Flow<List<CollectionTextEntity>>

    @Query("SELECT * FROM collection_text WHERE category = :categoryId")
    fun getCollectionTextsByCategory(categoryId: Long): Flow<List<CollectionTextEntity>>
}