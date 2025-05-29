package top.tsukino.llmdemo.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.data.repo.base.ProviderRepo
import javax.inject.Inject
import top.tsukino.llmdemo.feature.common.helper.withScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.config.LLMPreferences
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.repo.base.ModelRepo

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val providerRepo: ProviderRepo,
    private val modelRepo: ModelRepo,
    private val preferences: LLMPreferences,
): ViewModel() {
    private val _providersFlow = MutableStateFlow<List<ProviderEntity>>(emptyList())
    val providersFlow = _providersFlow.asStateFlow()

    private val _modelsFlow = MutableStateFlow<List<ModelEntity>>(emptyList())
    val modelsFlow = _modelsFlow.asStateFlow()


    init {
        loadProviders()
        loadModels()
        loadSummaryTitle()
    }

    private fun loadProviders() {
        withScope {
            viewModelScope.launch {
                providerRepo.getProviders().collect { providers ->
                    _providersFlow.value = providers
                }
            }
        }
    }

    private fun loadModels() {
        withScope {
            viewModelScope.launch {
                modelRepo.getModels().collect { models ->
                    _modelsFlow.value = models
                }
            }
        }
    }

    internal fun addProvider(providerEntity: ProviderEntity) {
        withScope {
            viewModelScope.launch {
                val id = providerRepo.insertProvider(providerEntity)
                val provider = providerRepo.getProvider(id)!!
                providerRepo.updateProviderModels(provider)
                val models = modelRepo.getModelsByProviderId(id)
                models.collect { models ->
                    Log.d("SettingsViewModel", "updateProviderModels: $models")
                }
            }
        }
    }

    internal fun deleteProvider(provider: ProviderEntity) {
        withScope {
            viewModelScope.launch {
                providerRepo.deleteProvider(provider)
                modelRepo.deleteModelsByProviderId(provider.id)
            }
        }
    }

    internal fun getProviderModels(provider: ProviderEntity) {
        withScope {
            viewModelScope.launch {
                providerRepo.updateProviderModels(provider)
                val models = modelRepo.getModelsByProviderId(provider.id)
                models.collect { models ->
                    Log.d("SettingsViewModel", "getModels: $models")
                }
            }
        }
    }

    private val _enableSummaryTitle = MutableStateFlow<Boolean>(false)
    val enableSummaryTitle = _enableSummaryTitle.asStateFlow()
    private val _taskModelName = MutableStateFlow<String>("")
    val taskModelName = _taskModelName.asStateFlow()
    private val _defaultModelName = MutableStateFlow<String>("")
    val defaultModelName = _defaultModelName.asStateFlow()
    private val _sttModelName = MutableStateFlow<String>("")
    val sttModelName = _sttModelName.asStateFlow()

    private fun loadSummaryTitle() {
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                launch(Dispatchers.IO) {
                    preferences.enableSummaryTitle.flow.collect { enable ->
                        _enableSummaryTitle.value = enable
                    }
                }
                launch(Dispatchers.IO) {
                    preferences.taskModelId.flow.collect { modelName ->
                        _taskModelName.value = modelName
                    }
                }
                launch(Dispatchers.IO) {
                    preferences.defaultModelId.flow.collect { modelName ->
                        _defaultModelName.value = modelName
                    }
                }
                launch(Dispatchers.IO) {
                    preferences.sttModelId.flow.collect { modelName ->
                        _sttModelName.value = modelName
                    }
                }
            }
        }
    }

    internal fun updateEnableSummaryTitle(enable: Boolean) {
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                preferences.enableSummaryTitle.set(enable)
            }
        }
    }

    internal fun updateTaskModelId(modelId: String) {
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                preferences.taskModelId.set(modelId)
            }
        }
    }

    internal fun updateDefaultModelId(modelId: String) {
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                preferences.defaultModelId.set(modelId)
            }
        }
    }

    internal fun updateSttModelId(modelId: String) {
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                preferences.sttModelId.set(modelId)
            }
        }
    }
}