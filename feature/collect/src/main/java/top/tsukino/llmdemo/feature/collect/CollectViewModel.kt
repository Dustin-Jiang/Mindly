package top.tsukino.llmdemo.feature.collect

import android.content.Context
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.api.LLMDemoApi
import top.tsukino.llmdemo.config.LLMPreferences
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.data.database.entity.RecordingEntity
import top.tsukino.llmdemo.data.recorder.AudioRecorder
import top.tsukino.llmdemo.data.repo.base.CollectionTextRepo
import top.tsukino.llmdemo.data.repo.base.ModelRepo
import top.tsukino.llmdemo.data.repo.base.ProviderRepo
import top.tsukino.llmdemo.data.repo.base.RecordingRepo
import top.tsukino.llmdemo.feature.collect.items.ItemId
import top.tsukino.llmdemo.feature.collect.items.toItemId
import top.tsukino.llmdemo.feature.common.MainController
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CollectViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordingRepo: RecordingRepo,
    private val collectionTextRepo: CollectionTextRepo,
    private val providerRepo: ProviderRepo,
    private val modelRepo: ModelRepo,
    private val api: LLMDemoApi,
    private val preferences: LLMPreferences
) : ViewModel() {
    val recorder = AudioRecorder(context)
    lateinit var mainController: MainController

    private val _recordingList = MutableStateFlow<List<RecordingEntity>>(emptyList())
    val recordingList = _recordingList.asStateFlow()

    private val _collectionTextList = MutableStateFlow<List<CollectionTextEntity>>(emptyList())
    val collectionTextList = _collectionTextList.asStateFlow()

    private val _currentShowing = MutableStateFlow<ItemId?>(null)
    val currentShowing = _currentShowing.asStateFlow()

    private val _providerFlow = MutableStateFlow<List<ProviderEntity>>(emptyList())
    private val _modelFlow = MutableStateFlow<List<ModelEntity>>(emptyList())
    private val _sttModelName = MutableStateFlow<String?>(null)
    private val _immediateTranscript = MutableStateFlow(false)
    private val _enableAutoSummaryTitle = MutableStateFlow(false)
    private val _summaryModelName = MutableStateFlow<String?>(null)

    val enableTranscript = derivedStateOf {
        val sttModel = _modelFlow.value.firstOrNull { _sttModelName.value == it.modelId }
        sttModel != null;
    }
    val enableSummaryTitle = derivedStateOf {
        val summaryModel = _modelFlow.value.firstOrNull { _summaryModelName.value == it.modelId }
        summaryModel != null;
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recordingRepo.getRecordingList().collect(_recordingList::emit)
        }
        viewModelScope.launch(Dispatchers.IO) {
            collectionTextRepo.getCollectionTextList().collect(_collectionTextList::emit)
        }
        viewModelScope.launch(Dispatchers.IO) {
            providerRepo.getProviders().collect { providers ->
                _providerFlow.value = providers
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            modelRepo.getModels().collect { models ->
                _modelFlow.value = models
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            preferences.sttModelId.flow.collect { modelName ->
                _sttModelName.value = modelName
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            preferences.taskModelId.flow.collect { modelName ->
                _summaryModelName.value = modelName
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            preferences.enableSummaryTitle.flow.collect { enable ->
                _enableAutoSummaryTitle.value = enable
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            preferences.immediateTranscript.flow.collect { enable ->
                _immediateTranscript.value = enable
            }
        }
    }

    internal fun startRecording() {
        try {
            recorder.start()
            Log.d("CollectViewModel", "Recording started successfully")
        } catch (e: Exception) {
            Log.e("CollectViewModel", "Failed to start recording", e)
            mainController.scope.launch {
                mainController.snackbarHostState.showSnackbar(
                    message = "开始录音失败: ${e.message}",
                    withDismissAction = true
                )
            }
        }
    }

    internal fun stopRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recording = recorder.stop()
                Log.d("CollectViewModel", "Recording stopped: ${recording.path}")
                viewModelScope.launch(Dispatchers.IO) {
                    val id = recordingRepo.insertRecording(recording)
                    if (_immediateTranscript.value) {
                        Log.d("CollectViewModel", "Immediate transcription enabled, transcribing recording")
                        transcriptRecording(id)
                    }
                }
            } catch (e: Exception) {
                Log.e("CollectViewModel", "Failed to stop recording", e)
                mainController.scope.launch {
                    mainController.snackbarHostState.showSnackbar(
                        message = "停止录音失败: ${e.message}",
                        withDismissAction = true
                    )
                }
            }
        }
    }

    internal fun setCurrentShowing(id: ItemId) {
        _currentShowing.value = id
    }
    internal fun clearCurrentShowing() {
        _currentShowing.value = null
    }

    val _showRecordingManageSheet = MutableStateFlow<Long?>(null)
    val showRecordingManageSheet = _showRecordingManageSheet.asStateFlow()

    internal fun showRecordingManageSheet(id: Long?) {
        _showRecordingManageSheet.value = id
    }

    val _showTextManageSheet = MutableStateFlow<Long?>(null)
    val showTextManageSheet = _showTextManageSheet.asStateFlow()

    internal fun showTextManageSheet(id: Long?) {
        _showTextManageSheet.value = id
    }

    internal fun deleteRecording(id: Long) {
        val item = _recordingList.value.find { it.id == id }
        item?.let {
            viewModelScope.launch(Dispatchers.IO) {
                recordingRepo.deleteRecording(it)
            }
        }
        _showRecordingManageSheet.value = null
    }

    internal fun deleteTextItem(id: Long) {
        val item = _collectionTextList.value.find { it.id == id }
        item?.let {
            viewModelScope.launch(Dispatchers.IO) {
                collectionTextRepo.deleteCollectionText(it)
            }
        }
        _showTextManageSheet.value = null
    }
    
    internal fun transcriptRecording(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = recordingRepo.getRecording(id)
            if (item == null) {
                Log.w("CollectViewModel", "No recording found with id: $id")
                return@launch
            }
            setCurrentShowing(item.toItemId())
            val sttModel = _modelFlow.value.firstOrNull { _sttModelName.value == it.modelId }
            sttModel?.let { selectedModel ->
                val providerName = _providerFlow.value.firstOrNull { it.id == selectedModel.providerId }?.name
                providerName?.let { providerName ->
                    mainController.scope.launch(Dispatchers.IO) {
                        try {
                            val transcript = api.getProvider(providerName)?.sendTranscript(
                                model = selectedModel.modelId,
                                file = File(context.getExternalFilesDir(null), item.path)
                            )
                            Log.d("CollectViewModel", "Transcript for ${item.path}: ${transcript?.text}")
                            recordingRepo.updateRecording(
                                item.copy(transcript = transcript?.text ?: "")
                            )
                            if (_enableAutoSummaryTitle.value && transcript?.text?.isNotEmpty() == true) {
                                recordingSummary(item.id)
                            }
                        } catch (e: Exception) {
                            Log.e("CollectViewModel", "Error transcribing recording ${item.path}", e)
                        }
                    }
                }
            }
        }
    }

    internal fun recordingSummary(id: Long) {
        if (!enableSummaryTitle.value) {
            Log.w("CollectViewModel", "No task model selected")
            throw IllegalStateException("请先选择任务模型")
        }
        val item = _recordingList.value.find { it.id == id }
        item?.let { recordingItem ->
            if (recordingItem.transcript.isEmpty()) {
                Log.w("CollectViewModel", "Cannot summarize recording ${recordingItem.path} without transcript")
                return transcriptRecording(id)
            }
            handleSummary(
                content = recordingItem.transcript,
                onFinish = { summary ->
                    Log.d("CollectViewModel", "Summary for ${recordingItem.path}: $summary")
                    recordingRepo.updateRecording(
                        recordingItem.copy(title = summary)
                    )
                }
            )
        }
    }

    internal fun textSummary(id: Long) {
        if (!_enableAutoSummaryTitle.value) {
            Log.w("CollectViewModel", "Summary title generation is disabled")
            throw IllegalStateException("请先启用摘要标题生成")
        }
        val item = _collectionTextList.value.find { it.id == id }
        item?.let { item ->
            if (item.content.isEmpty()) {
                Log.w("CollectViewModel", "Cannot summarize ${item.id}")
                return
            }
            handleSummary(
                content = item.content,
                onFinish = { summary ->
                    Log.d("CollectViewModel", "Summary for ${item.id}: $summary")
                    collectionTextRepo.updateCollectionText(
                        item.copy(title = summary)
                    )
                }
            )
        }
    }

    internal fun handleSummary(
        content: String,
        onFinish: suspend (String) -> Unit
    ) {
        val summaryModel = _modelFlow.value.firstOrNull { _summaryModelName.value == it.modelId }
        if (summaryModel == null) {
            Log.w("CollectViewModel", "No model found for summary: ${_summaryModelName.value}")
            throw IllegalStateException("请先选择摘要模型")
        }
        val providerName = _providerFlow.value.firstOrNull { it.id == summaryModel.providerId }?.name
        providerName?.let { providerName ->
            mainController.scope.launch(Dispatchers.IO) {
                try {
                    api.getProvider(providerName)?.handleSummary(
                        model = summaryModel.modelId,
                        content = content,
                        onFinish = onFinish,
                    )
                } catch (e: Exception) {
                    Log.e("CollectViewModel", "Error summarizing ${content}", e)
                }
            }
        }
    }
}
