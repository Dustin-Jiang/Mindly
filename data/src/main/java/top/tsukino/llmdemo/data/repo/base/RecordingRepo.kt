package top.tsukino.llmdemo.data.repo.base

import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.entity.RecordingEntity

interface RecordingRepo {
    suspend fun getRecording(id: Long) : RecordingEntity?

    fun getRecordingList(): Flow<List<RecordingEntity>>

    suspend fun insertRecording(recording: RecordingEntity): Long
    suspend fun deleteRecording(recording: RecordingEntity)
    suspend fun updateRecording(recording: RecordingEntity)
}