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
import top.tsukino.llmdemo.data.database.entity.RecordingEntity
import top.tsukino.llmdemo.data.recorder.AudioRecorder
import top.tsukino.llmdemo.data.repo.base.RecordingRepo
import javax.inject.Inject

@HiltViewModel
class CollectViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordingRepo: RecordingRepo,
) : ViewModel() {
    val recorder = AudioRecorder(context)

    private val _recordingList = MutableStateFlow<List<RecordingEntity>>(emptyList())
    val recordingList = _recordingList.asStateFlow()

    private val _currentShowing = MutableStateFlow<String?>(null)
    val currentShowing = _currentShowing.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recordingRepo.getRecordingList().collect(_recordingList::emit)
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

    internal fun setCurrentShowing(id: String) {
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
}