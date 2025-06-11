package top.tsukino.mindly.data.recorder

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.runBlocking
import top.tsukino.mindly.data.database.entity.RecordingEntity
import java.io.File
import java.io.IOException
import java.util.Date

class AudioRecorder(
    private val context: Context
) {
    private var fileName: String = ""
    private var outputFile: File? = null
    private var recorder: MediaRecorder? = null

    private var status: RecordingState = RecordingState.Idle

    private val converter = AudioConverter()
    private val bitrate = 240 * 1024

    fun prepare() {
        status = RecordingState.Preparing
        fileName = "recording_${System.currentTimeMillis()}"
        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ".webm"
        } else {
            ".m4a"
        }
        outputFile = File(context.getExternalFilesDir(null), "$fileName$format")
        if (outputFile!!.exists()) {
            outputFile!!.delete()
        }
        Log.d("AudioRecorder", "Output file prepared: ${outputFile!!.absolutePath}")
    }

    fun start() {
        if (status != RecordingState.Idle) {
            throw IllegalStateException("Recorder is not in Idle state. Current state: $status")
        }
        recorder = MediaRecorder()
        prepare()
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setOutputFormat(MediaRecorder.OutputFormat.WEBM)
                setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
            }
            else {
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            }
            // 设置音频编码比特率为 240kbps
            setAudioEncodingBitRate(bitrate)
            setAudioSamplingRate(44100)
            setOutputFile(outputFile!!.absolutePath)
            prepare()
        }
        recorder?.let {
            try {
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

    suspend fun stop(): RecordingEntity {
        if (outputFile == null) {
            throw IllegalStateException("Output file is not prepared. Call prepare() before stop().")
        }

        if (status != RecordingState.Recording) {
            throw IllegalStateException("Recorder is not in Recording state. Current state: $status")
        }

        var duration = 0L
        recorder?.let {
            try {
                it.stop()
                status = RecordingState.Stopped

                if (outputFile!!.exists()) {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(outputFile!!.absolutePath)
                    val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    duration = durationString?.toLongOrNull() ?: 0L // 时长是字符串，单位是毫秒
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        val converted = File(context.getExternalFilesDir(null), "${fileName}.mp3")
                        converter.encode(outputFile!!, converted)
                        outputFile!!.delete()
                        outputFile = converted
                    }
                }
            } catch (e: RuntimeException) {
                status = RecordingState.Error("Failed to stop recording: ${e.message}")
                e.printStackTrace()
            } finally {
                release()
            }
        }
        recorder = null

        return RecordingEntity(
            id = 0L,
            path = outputFile!!.name,
            title = "新速记",
            duration = duration,
            timestamp = Date(),
        )
    }

    private fun release() {
        recorder?.release()
        recorder = null
        status = RecordingState.Idle
    }

    fun cancel() {
        recorder?.let {
            if (status == RecordingState.Recording) {
                runBlocking { stop() }
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