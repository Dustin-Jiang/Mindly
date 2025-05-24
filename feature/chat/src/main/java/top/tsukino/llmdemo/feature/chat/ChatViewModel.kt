package top.tsukino.llmdemo.feature.chat

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.core.Role
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import top.tsukino.llmdemo.api.LLMDemoApi
import top.tsukino.llmdemo.config.LLMPreferences
import top.tsukino.llmdemo.data.database.dto.ConversationWithMessages
import top.tsukino.llmdemo.data.database.entity.MessageEntity
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.data.database.entity.toChatMessage
import top.tsukino.llmdemo.data.repo.base.ConversationRepo
import top.tsukino.llmdemo.data.repo.base.ModelRepo
import top.tsukino.llmdemo.data.repo.base.ProviderRepo
import top.tsukino.llmdemo.feature.common.helper.withScope
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val conversationRepo: ConversationRepo,
    private val providerRepo: ProviderRepo,
    private val modelRepo: ModelRepo,
    private val api: LLMDemoApi,
    private val preferences: LLMPreferences,
): ViewModel() {
    private val _conversationState = MutableStateFlow<ConversationWithMessages?>(null)
    val conversationState: StateFlow<ConversationWithMessages?> = _conversationState.asStateFlow()

    internal fun load(conversationId: Long) {
        loadProviders()
        loadConversation(conversationId)
        loadSummaryTitle()
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                loadDefaultModel()
            }
        }
    }

    private var job: Job? = null
    internal fun loadConversation(id: Long) {
        withScope {
            job?.cancel()
            job = viewModelScope.launch {
                conversationRepo.getConversation(id).collect(_conversationState::emit)
            }
        }
    }


    private val _inputFlow = MutableStateFlow<InputData>(InputData.empty())
    val inputFlow: StateFlow<InputData> = _inputFlow.asStateFlow()

    internal fun updateInputData(inputData: InputData) {
        _inputFlow.value = inputData
    }

    internal fun clearInputData() {
        _inputFlow.value = InputData.empty()
    }


    private val _providerFlow = MutableStateFlow<List<ProviderEntity>>(emptyList())
    val providerFlow: StateFlow<List<ProviderEntity>> = _providerFlow.asStateFlow()
    
    internal fun loadProviders() {
        withScope {
            viewModelScope.launch {
                providerRepo.getProviders().collect { providers ->
                    _providerFlow.value = providers
                }
            }
        }
    }

    private val _modelListFlow = MutableStateFlow<List<ModelEntity>>(emptyList())
    val modelListFlow: StateFlow<List<ModelEntity>> = _modelListFlow.asStateFlow()

    internal suspend fun loadModels() {
        val selected = conversationState.value?.conversation?.selectedModel
            ?: _defaultModelName.value.ifEmpty { "" }

        modelRepo.getModels().collect { models ->
            _modelListFlow.value = models
            models.find {
                it.modelId == selected
            }?.let {
                _modelFlow.value = it
            }
            return@collect
        }
    }

    private val _enableSummaryTitle = MutableStateFlow(false)
    private val _taskModelName = MutableStateFlow("")

    internal fun loadSummaryTitle() {
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
            }
        }
    }

    private val _defaultModelName = MutableStateFlow("")
    internal suspend fun loadDefaultModel() {
        preferences.defaultModelId.flow.collect { modelName ->
            _defaultModelName.value = modelName
            loadModels()
        }
    }

    private val _modelFlow = MutableStateFlow<ModelEntity?>(null)
    val modelFlow: StateFlow<ModelEntity?> = _modelFlow.asStateFlow()

    internal fun selectModel(model: ModelEntity) {
        _modelFlow.value = model
        viewModelScope.launch(Dispatchers.IO) {
            _conversationState.value?.conversation?.id?.let { id ->
                conversationRepo.updateSelectedModel(
                    id = id,
                    model = model.modelId
                )
            }
        }
    }


    internal fun addUserMessage(inputData: InputData, onUpdate: () -> Unit) {
        val userMsg = MessageEntity(
            id = 0L,
            conversationId = _conversationState.value?.conversation?.id ?: 0L,
            text = inputData.text,
            timestamp = Date(),
            isUser = true,
        )

        api.scope.launch(Dispatchers.IO) {
            conversationRepo.addMessage(userMsg)
            handleChat(userMsg, onUpdate)  // 将用户消息传递给handleChat
        }
    }

    internal suspend fun handleChat(lastUserMessage: MessageEntity, onUpdate: () -> Unit) {
        var replyMsg = MessageEntity(
            id = 0L,
            conversationId = _conversationState.value?.conversation?.id ?: 0L,
            text = "",
            timestamp = Date(),
            isUser = false,
            model = modelFlow.value?.modelId,
        )

        val providerName = providerFlow.value.find { it -> it.id == modelFlow.value?.providerId }?.name
        Log.d("handleChat", "Provider name: $providerName")

        replyMsg = replyMsg.copy(id = conversationRepo.addMessage(replyMsg))

        providerName?.let {
            api.getProvider(it)?.let { provider ->
                provider.handleChat(
                    model = modelFlow.value?.modelId ?: "",
                    message = lastUserMessage.toChatMessage(),
                    history = _conversationState.value?.messages?.map { it.toChatMessage() } ?: emptyList(),
                    onUpdate = {
                        replyMsg.text += it
                        conversationRepo.updateMessage(replyMsg)
                        onUpdate()
                    },
                    onFinish = { endReason ->
                        replyMsg.endReason = endReason
                        conversationRepo.updateMessage(replyMsg)
                        handleSummaryTitle()
                    }
                )
            }
        }
    }

    internal suspend fun handleSummaryTitle() {
        Log.d("handleSummaryTitle", "${_taskModelName.value}, ${_enableSummaryTitle.value}")

        if (_taskModelName.value.isEmpty()) return
        if (!_enableSummaryTitle.value) return

        val taskModel = modelListFlow.value.find { it.modelId == _taskModelName.value }
        val providerName = providerFlow.value.find { it -> it.id == taskModel?.providerId }?.name

        Log.d("handleSummaryTitle", "Provider name: $providerName")

        providerName?.let { provider ->
            val chatContents = conversationState.value?.messages?.takeLast(4)?.joinToString("\n\n"){ it.text }
            chatContents?.let { content ->
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
                api.getProvider(provider)?.handleChat(
                    model = modelFlow.value?.modelId ?: "",
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
                        }
                        catch(e: Exception) {
                            Log.e("handleSummaryTitle", "Error parsing JSON: ${e.message}")
                        }
                        title?.let {
                            conversationRepo.updateConversationTitle(
                                id = _conversationState.value?.conversation?.id ?: 0L,
                                title = it,
                            )
                        }
                        return@handleChat
                    }
                )
            }
        }
    }
}