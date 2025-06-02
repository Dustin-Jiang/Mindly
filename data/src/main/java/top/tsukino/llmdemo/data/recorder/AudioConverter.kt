package top.tsukino.llmdemo.data.recorder

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import com.naman14.androidlame.AndroidLame
import java.io.File
import java.io.FileOutputStream

class AudioConverter() {
    private val androidLame: AndroidLame = AndroidLame()

    suspend fun encode(
        input: File,
        output: File,
    ) {
        val extractor = MediaExtractor()
        extractor.setDataSource(input.absolutePath)
        val trackIndex = (0 until extractor.trackCount).firstOrNull {
            extractor.getTrackFormat(it).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
        } ?: throw IllegalArgumentException("No audio track found")

        extractor.selectTrack(trackIndex)
        val format = extractor.getTrackFormat(trackIndex)
        val mime = format.getString(MediaFormat.KEY_MIME) ?: throw IllegalArgumentException("MIME type not found")
        val decoder = MediaCodec.createDecoderByType(mime)
        decoder.configure(format, null, null, 0)
        decoder.start()

        val outputStream = FileOutputStream(output.absolutePath)
        val bufferInfo = MediaCodec.BufferInfo()
        val timeoutUs = 10000L
        val pcmBuffer = ShortArray(1024)
        val mp3Buffer = ByteArray(1024)

        while (true) {
            val inputBufferIndex = decoder.dequeueInputBuffer(timeoutUs)
            if (inputBufferIndex >= 0) {
                val inputBuffer = decoder.getInputBuffer(inputBufferIndex) ?: continue
                val sampleSize = extractor.readSampleData(inputBuffer, 0)
                if (sampleSize < 0) {
                    decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                    break
                } else {
                    decoder.queueInputBuffer(inputBufferIndex, 0, sampleSize, extractor.sampleTime, 0)
                    extractor.advance()
                }
            }

            val outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, timeoutUs)
            if (outputBufferIndex >= 0) {
                val outputBuffer = decoder.getOutputBuffer(outputBufferIndex) ?: continue
                outputBuffer.asShortBuffer().get(pcmBuffer, 0, bufferInfo.size / 2)
                val bytesEncoded = androidLame.encode(pcmBuffer, pcmBuffer, bufferInfo.size / 2, mp3Buffer)
                if (bytesEncoded > 0) {
                    outputStream.write(mp3Buffer, 0, bytesEncoded)
                }
                decoder.releaseOutputBuffer(outputBufferIndex, false)
            }
        }

        val flushResult = androidLame.flush(mp3Buffer)
        if (flushResult > 0) {
            outputStream.write(mp3Buffer, 0, flushResult)
        }

        outputStream.close()
        decoder.stop()
        decoder.release()
        extractor.release()

    }
}

