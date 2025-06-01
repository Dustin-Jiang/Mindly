package top.tsukino.llmdemo.data.repo

import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.LLMDemoDatabase
import top.tsukino.llmdemo.data.database.dao.RecodingDao
import top.tsukino.llmdemo.data.database.entity.RecordingEntity
import top.tsukino.llmdemo.data.repo.base.RecordingRepo
import javax.inject.Inject

class DefaultRecordingRepo @Inject constructor(
    private val database: LLMDemoDatabase
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
        return database.recordingDao().deleteRecording(recording)
    }

    override suspend fun updateRecording(
        recording: RecordingEntity
    ) {
        return database.recordingDao().updateRecording(recording)
    }
}