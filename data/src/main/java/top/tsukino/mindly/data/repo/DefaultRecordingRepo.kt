package top.tsukino.mindly.data.repo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.MindlyDatabase
import top.tsukino.mindly.data.database.entity.RecordingEntity
import top.tsukino.mindly.data.repo.base.RecordingRepo
import java.io.File
import javax.inject.Inject

class DefaultRecordingRepo @Inject constructor(
    private val database: MindlyDatabase,
    @ApplicationContext private val context: Context
): RecordingRepo {
    override suspend fun getRecording(
        id: Long
    ): RecordingEntity? {
        return database.recordingDao().getRecordingById(id)
    }

    override fun getRecordingList(): Flow<List<RecordingEntity>> {
        return database.recordingDao().getAllRecordings()
    }

    override suspend fun insertRecording(
        recording: RecordingEntity
    ): Long {
        return database.recordingDao().insertRecording(recording)
    }

    override suspend fun deleteRecording(
        recording: RecordingEntity
    ) {
        val path = recording.path
        val file = File(context.getExternalFilesDir(null), path)
        if (file.exists()) {
            if (!file.delete()) {
                throw IllegalStateException("Failed to delete file: $path")
            }
        }
        return database.recordingDao().deleteRecording(recording)
    }

    override suspend fun updateRecording(
        recording: RecordingEntity
    ) {
        return database.recordingDao().updateRecording(recording)
    }

    override fun getRecordingsByCategory(
        categoryId: Long
    ): Flow<List<RecordingEntity>> {
        return database.recordingDao().getRecordingsByCategory(categoryId)
    }
}