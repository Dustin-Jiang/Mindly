package top.tsukino.llmdemo.data.recorder

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class AudioRecorder(
    private val context: Context
) {
    private var outputFile: File? = null
    private var recorder: MediaRecorder? = null

    private var status: RecordingState = RecordingState.Idle

    fun prepare() {
        status = RecordingState.Preparing
        val fileName = "recording_${System.currentTimeMillis()}.m4a"
        outputFile = File(context.getExternalFilesDir(null), fileName)
        if (outputFile!!.exists()) {
            outputFile!!.delete()
        }
        recorder?.apply {
            prepare()
        }
    }

    fun start() {
        if (status != RecordingState.Idle) {
            throw IllegalStateException("Recorder is not in Idle state. Current state: $status")
        }
        recorder = MediaRecorder().also {
            it.apply {
                setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile!!.absolutePath)
            }
        }
        recorder?.let {
            try {
                status = RecordingState.Preparing
                prepare()
                it.start()
                status = RecordingState.Recording
            } catch (e: IOException) {
                status = RecordingState.Error("Failed to start recording: ${e.message}")
                e.printStackTrace()
                release()
            } catch (e: RuntimeException) {
                status = RecordingState.Error("Failed to start recording: ${e.message}")
                e.printStackTrace()
                release()
            }
        }
    }

    fun stop() {
        recorder?.let {
            try {
                it.stop()
                status = RecordingState.Stopped
            } catch (e: RuntimeException) {
                status = RecordingState.Error("Failed to stop recording: ${e.message}")
                e.printStackTrace()
            } finally {
                release()
            }
        }
        recorder = null
    }

    private fun release() {
        recorder?.release()
        recorder = null
        status = RecordingState.Idle
    }

    fun cancel() {
        recorder?.let {
            if (status == RecordingState.Recording) {
                stop()
            }
            release()
        }
        recorder = null
        if (outputFile!!.exists()) {
            outputFile!!.delete()
        }
    }

    fun getOutputFile(): File? = outputFile
}