package top.tsukino.llmdemo.data.repo.base

import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.entity.ProviderEntity

interface ProviderRepo {
    suspend fun getProvider(id: Long): ProviderEntity?

    fun getProviders(): Flow<List<ProviderEntity>>

    suspend fun insertProvider(provider: ProviderEntity): Long

    suspend fun updateProvider(provider: ProviderEntity)

    suspend fun deleteProvider(provider: ProviderEntity)

    suspend fun updateProviderModels(provider: ProviderEntity)
}