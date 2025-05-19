package top.tsukino.llmdemo.data.repo

import top.tsukino.llmdemo.api.LLMDemoApi
import top.tsukino.llmdemo.data.database.LLMDemoDatabase
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.repo.base.ModelRepo
import javax.inject.Inject

class DefaultModelRepo @Inject constructor(
    private val api: LLMDemoApi,
    private val database: LLMDemoDatabase,
): ModelRepo {
    override suspend fun insertModel(model: ModelEntity) = database.modelDao().insertModel(model)

    override suspend fun updateModel(model: ModelEntity) = database.modelDao().updateModel(model)

    override suspend fun deleteModel(model: ModelEntity) = database.modelDao().deleteModel(model)

    override suspend fun getModel(id: Long) = database.modelDao().getModelById(id)

    override fun getModelsByProviderId(providerId: Long) = database.modelDao().getModelsByProviderId(providerId)

    override suspend fun deleteModelsByProviderId(providerId: Long) = database.modelDao().deleteModelsByProviderId(providerId)

    override fun getModels() = database.modelDao().getModels()
}