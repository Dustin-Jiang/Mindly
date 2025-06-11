package top.tsukino.mindly.data.repo.base

import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.entity.ModelEntity

interface ModelRepo {
    suspend fun getModel(id: Long): ModelEntity?

    fun getModelsByProviderId(providerId: Long): Flow<List<ModelEntity>>

    suspend fun insertModel(model: ModelEntity): Long

    suspend fun updateModel(model: ModelEntity)

    suspend fun deleteModel(model: ModelEntity)

    suspend fun deleteModelsByProviderId(providerId: Long)

    fun getModels(): Flow<List<ModelEntity>>
}