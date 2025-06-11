package top.tsukino.mindly.data.repo

import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import top.tsukino.mindly.api.ApiConfig
import top.tsukino.mindly.api.LLMDemoApi
import top.tsukino.mindly.data.database.MindlyDatabase
import top.tsukino.mindly.data.database.entity.ModelEntity
import top.tsukino.mindly.data.database.entity.ProviderEntity
import top.tsukino.mindly.data.repo.base.ProviderRepo
import javax.inject.Inject

class DefaultProviderRepo @Inject constructor(
    private val database: MindlyDatabase,
    private val api: LLMDemoApi,
): ProviderRepo {
    override fun getProviders() = database.providerDao().getAllProviders()
    override suspend fun getProvider(id: Long) = database.providerDao().getProviderById(id)
    override suspend fun updateProvider(item: ProviderEntity) = database.providerDao().updateProvider(item)

    override suspend fun insertProvider(item: ProviderEntity): Long {
        val id = database.providerDao().insertProvider(item)
        api.addProvider(name = item.name, config = ApiConfig(
            token = item.token,
            host = item.host
        ))
        return id
    }

    override suspend fun deleteProvider(item: ProviderEntity) = database.providerDao().deleteProvider(item)

    override suspend fun updateProviderModels(providerEntity: ProviderEntity) {
        Log.d("DefaultProviderRepo", "updateProviderModels: trying update ${providerEntity.name}")
        
        api.getProvider(providerEntity.name)?.let { provider ->
            try {
                // 使用 coroutineScope 确保所有操作在同一作用域内完成
                coroutineScope {
                    // 先清除该 provider 的所有旧模型数据
                    database.modelDao().deleteModelsByProviderId(providerEntity.id)
                    provider.getModelIds()
                        .catch { e ->
                            Log.e("DefaultProviderRepo", "Error collecting models for ${providerEntity.name}", e)
                        }
                        .collect { ids ->
                            Log.d("DefaultProviderRepo", "Received models for ${providerEntity.name}: $ids")
                            ids.forEach { id ->
                                database.modelDao().insertModel(
                                    ModelEntity(providerId = providerEntity.id, modelId = id)
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Log.e("DefaultProviderRepo", "Failed to update models for ${providerEntity.name}", e)
                throw e
            }
        } ?: run {
            Log.e("DefaultProviderRepo", "Provider ${providerEntity.name} not found")
        }
    }
}