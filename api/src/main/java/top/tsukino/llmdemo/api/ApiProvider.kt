package top.tsukino.llmdemo.api

import android.util.Log
import com.aallam.openai.api.audio.Transcription
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.file.FileSource
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
import kotlinx.io.RawSource
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.json.JSONObject
import java.io.File

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

    suspend fun handleSummary(
        model: String,
        content: String,
        onFinish: suspend (title: String) -> Unit,
    ) {
        Log.d("handleSummaryTitle", "Chat contents: $content")

        val summaryMessage = ChatMessage(
            content = "Create an emoji-based title that concisely reflects the chat's main topic (max 12 words, same language as the chat). Avoid quotes or special formatting; the title must start with a relevant emoji.\n" +
                    "\n" +
                    "Output as JSON: { \"title\": \"Your Title\" }\n" +
                    "\n" +
                    "Examples:\n" +
                    "- { \"title\": \"\uD83D\uDCC9 Stock Market Basics\" }\n" +
                    "\n" +
                    "Chat History:\n" +
                    "<chat_history>\n" +
                    "$content\n" +
                    "</chat_history>",
            role = Role.User
        )

        val title: MutableList<String> = mutableListOf()
        handleChat(
            model = model,
            message = summaryMessage,
            history = emptyList(),
            onUpdate = {
                title.add(it)
            },
            onFinish = { endReason ->
                val titleResult = title.joinToString("")
                var title: String? = null
                try {
                    title = JSONObject(titleResult).getString("title")
                    title?.let { onFinish(it) }
                }
                catch(e: Exception) {
                    Log.e("handleSummaryTitle", "Error parsing JSON: ${e.message}")
                }
                return@handleChat
            }
        )
    }

    suspend fun sendTranscript(
        model: String,
        file: File
    ): Transcription {
        val modelId = models.value.find { it.id.id == model }?.id
            ?: throw IllegalArgumentException("Model not found: $model")

        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Invalid file path: ${file.absolutePath}")
        }

        Log.d("ApiProvider", "Sending transcript request to model: $model for file ${file.absolutePath}")
        val transcriptRequest = TranscriptionRequest(
            model = modelId,
            audio = FileSource(file.name, file.toRawSource()),
            prompt = "You are an experienced assistant, please help transcribing this audio into precise text.",
        )

        val completions = client.transcription(request = transcriptRequest)
        return completions
    }
    
    internal fun File.toRawSource(): RawSource {
        val path = Path(this.absolutePath)
        return SystemFileSystem.source(path)
    }
}