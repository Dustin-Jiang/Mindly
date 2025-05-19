package top.tsukino.llmdemo.api

import android.content.Context
import com.aallam.openai.api.model.Model
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LLMDemoApi() {
    private val configs = mutableMapOf<String, OpenAIConfig>()
    private val providers = mutableMapOf<String, ApiProvider>()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _models: MutableStateFlow<List<Model>> = MutableStateFlow(emptyList())
    val models: StateFlow<List<Model>> = _models

    fun addProvider(name: String, config: ApiConfig) {
        configs[name] = config.toOpenAIConfig()

        val provider = ApiProvider(config.toOpenAIConfig())
        providers[name] = provider
    }

    fun getProvider(name: String): ApiProvider? {
        return providers[name]
    }

    fun getModels() : Flow<List<Model>> {
        return models
    }
}