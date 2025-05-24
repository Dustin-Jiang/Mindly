package top.tsukino.llmdemo.api

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.model.Model
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class ApiProvider(
    config: OpenAIConfig
) {
    val client = OpenAI(config)
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _models = MutableStateFlow<List<Model>>(emptyList())
    val models = _models.asStateFlow()

    init {
        Log.d("ApiProvider", "Initializing ApiProvider")
        loadModels()
    }

    private fun loadModels() {
        scope.launch {
            try {
                val modelList = client.models()
                Log.d("ApiProvider", "Loaded ${modelList.size} models")
                _models.emit(modelList)
            } catch (e: Exception) {
                Log.e("ApiProvider", "Error loading models", e)
            }
        }
    }

    fun getModels(): List<Model> = models.value

    fun getModelIds(): Flow<List<String>> = models
        .transform { modelsList ->
            Log.d("ApiProvider", "Transforming models, current size: ${modelsList.size}")
            if (modelsList.isEmpty()) {
                Log.d("ApiProvider", "Models list is empty, reloading...")
                loadModels()
            } else {
                val ids = modelsList.map { model -> 
                    Log.d("ApiProvider", "Processing model: ${model.id.id}")
                    model.id.id 
                }
                emit(ids)
            }
        }

    fun sendMessage(
        model: String,
        messages: List<ChatMessage>,
    ): Flow<ChatCompletionChunk> {
        val modelId = models.value.find { it.id.id == model }?.id
            ?: throw IllegalArgumentException("Model not found: $model")

        Log.d("ApiProvider", "Sending message to model: $model")
        val chatCompletionRequest = ChatCompletionRequest(
            model = modelId,
            messages = messages
        )
        val completions: Flow<ChatCompletionChunk> = client.chatCompletions(chatCompletionRequest)
        return completions
    }

    val DEFAULT_SYSTEM_PROMPT = "You are a helpful assistant, always answering question in a friendly and informative manner. " +
            "If you don't know the answer, just say 'I don't know'. " +
            "If the question is not clear, ask for clarification. "

    suspend fun handleChat(
        model: String,
        message: ChatMessage,
        history: List<ChatMessage>,
        onUpdate: suspend (content: String) -> Unit,
        onFinish: suspend (finishReason: String) -> Unit,
    ) {
        val history = history.toMutableList()
        // 确保最后的用户消息被包含在内
        history.add(message)

        val systemMsg = ChatMessage(
            role = Role.System,
            content = DEFAULT_SYSTEM_PROMPT
        )
        history.add(0, systemMsg)

        Log.d("handleChat", "Messages: $history")
        sendMessage(model, history).collect { response ->
            val content: MutableList<String> = mutableListOf()
            response.choices.forEach {
                it.delta?.content?.let {
                    Log.d("handleChat", "Received chunk: ${it}")
                    content.add(it)
                }
                it.finishReason?.value?.let {
                    Log.d("handleChat", "Received finish reason: ${it}")
                    onFinish(it)
                    return@collect
                }
            }
            if (content.isNotEmpty()) {
                onUpdate(content.joinToString(""))
            }
        }
    }
}