package top.tsukino.mindly.data.repo

import top.tsukino.mindly.api.MindlyApi
import top.tsukino.mindly.data.database.MindlyDatabase
import top.tsukino.mindly.data.database.entity.ModelEntity
import top.tsukino.mindly.data.repo.base.ModelRepo
import javax.inject.Inject

class DefaultModelRepo @Inject constructor(
    private val api: MindlyApi,
    private val database: MindlyDatabase,
): ModelRepo {
    override suspend fun insertModel(model: ModelEntity) = database.modelDao().insertModel(model)

    override suspend fun updateModel(model: ModelEntity) = database.modelDao().updateModel(model)

    override suspend fun deleteModel(model: ModelEntity) = database.modelDao().deleteModel(model)

    override suspend fun getModel(id: Long) = database.modelDao().getModelById(id)

    override fun getModelsByProviderId(providerId: Long) = database.modelDao().getModelsByProviderId(providerId)

    override suspend fun deleteModelsByProviderId(providerId: Long) = database.modelDao().deleteModelsByProviderId(providerId)

    override fun getModels() = database.modelDao().getModels()
}