package top.tsukino.llmdemo.data.recorder

sealed class RecordingState {
    object Idle : RecordingState()
    object Preparing : RecordingState()
    object Recording : RecordingState()
    object Paused : RecordingState()
    object Stopped : RecordingState()
    data class Error(val message: String) : RecordingState()
}