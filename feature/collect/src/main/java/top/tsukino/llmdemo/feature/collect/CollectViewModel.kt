package top.tsukino.llmdemo.feature.collect

import android.content.Context
import android.util.Log
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
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.data.database.entity.RecordingEntity
import top.tsukino.llmdemo.data.recorder.AudioRecorder
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
    private val providerRepo: ProviderRepo,
    private val modelRepo: ModelRepo,
    private val api: LLMDemoApi,
    private val preferences: LLMPreferences
) : ViewModel() {
    val recorder = AudioRecorder(context)
    lateinit var mainController: MainController

    private val _recordingList = MutableStateFlow<List<RecordingEntity>>(emptyList())
    val recordingList = _recordingList.asStateFlow()

    private val _currentShowing = MutableStateFlow<ItemId?>(null)
    val currentShowing = _currentShowing.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recordingRepo.getRecordingList().collect(_recordingList::emit)
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
    }

    internal fun startRecording() {
        recorder.start()
    }

    internal fun stopRecording() {
        val recording = recorder.stop()
        Log.d("CollectViewModel", "Recording stopped: ${recording.path}")
        viewModelScope.launch(Dispatchers.IO) {
            recordingRepo.insertRecording(recording)
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

    internal fun deleteRecording(id: Long) {
        val item = _recordingList.value.find { it.id == id }
        item?.let {
            viewModelScope.launch(Dispatchers.IO) {
                recordingRepo.deleteRecording(it)
            }
        }
        _showRecordingManageSheet.value = null
    }

    private val _providerFlow = MutableStateFlow<List<ProviderEntity>>(emptyList())
    private val _modelFlow = MutableStateFlow<List<ModelEntity>>(emptyList())
    private val _sttModelName = MutableStateFlow<String?>(null)

    internal fun transcriptRecording(id: Long) {
        val item = _recordingList.value.find { it.id == id }
        item?.let { item ->
            setCurrentShowing(item.toItemId())
            val model = _modelFlow.value.firstOrNull { _sttModelName.value == it.modelId }
            model?.let { model ->
                val provider = _providerFlow.value.firstOrNull { it.id == model.providerId }?.name
                provider?.let { provider ->
                    mainController.scope.launch(Dispatchers.IO) {
                        try {
                            val transcript = api.getProvider(provider)?.sendTranscript(
                                model = model.modelId,
                                file = File(context.getExternalFilesDir(null), item.path)
                            )
                            Log.d("CollectViewModel", "Transcript for ${item.path}: ${transcript?.text}")
                            recordingRepo.updateRecording(
                                item.copy(transcript = transcript?.text ?: "")
                            )
                        } catch (e: Exception) {
                            Log.e("CollectViewModel", "Error transcribing recording ${item.path}", e)
                        }
                    }
                }
            }
        }
    }
}