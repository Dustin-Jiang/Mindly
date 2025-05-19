package top.tsukino.llmdemo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.entity.ModelEntity

@Dao
interface ModelDao {
    @Insert
    suspend fun insertModel(model: ModelEntity): Long

    @Update
    suspend fun updateModel(model: ModelEntity)

    @Delete
    suspend fun deleteModel(model: ModelEntity)

    @Query("SELECT * FROM model WHERE id = :id")
    suspend fun getModelById(id: Long): ModelEntity?

    @Query("SELECT * FROM model WHERE providerId = :providerId")
    fun getModelsByProviderId(providerId: Long): Flow<List<ModelEntity>>

    @Query("SELECT * FROM model")
    fun getModels(): Flow<List<ModelEntity>>

    @Query("DELETE FROM model WHERE providerId = :providerId")
    suspend fun deleteModelsByProviderId(providerId: Long)
}