package top.tsukino.llmdemo.feature.collect

import android.app.Application
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import top.tsukino.llmdemo.data.recorder.AudioRecorder
import javax.inject.Inject

@HiltViewModel
class CollectViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val recorder = AudioRecorder(context)

    internal fun startRecording() {
        recorder.start()
    }

    internal fun stopRecording() {
        recorder.stop()
    }
}