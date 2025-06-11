package top.tsukino.mindly.data.repo.base

import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.entity.ProviderEntity

interface ProviderRepo {
    suspend fun getProvider(id: Long): ProviderEntity?

    fun getProviders(): Flow<List<ProviderEntity>>

    suspend fun insertProvider(provider: ProviderEntity): Long

    suspend fun updateProvider(provider: ProviderEntity)

    suspend fun deleteProvider(provider: ProviderEntity)

    suspend fun updateProviderModels(provider: ProviderEntity)
}