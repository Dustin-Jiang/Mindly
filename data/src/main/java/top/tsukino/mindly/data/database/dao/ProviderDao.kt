package top.tsukino.mindly.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.entity.ProviderEntity

@Dao
interface ProviderDao {
    @Insert
    suspend fun insertProvider(provider: ProviderEntity): Long

    @Delete
    suspend fun deleteProvider(provider: ProviderEntity)

    @Update
    suspend fun updateProvider(provider: ProviderEntity)

    @Query("SELECT * FROM provider WHERE id = :id")
    suspend fun getProviderById(id: Long): ProviderEntity?

    @Query("SELECT * FROM provider")
    fun getAllProviders(): Flow<List<ProviderEntity>>
}