package top.tsukino.llmdemo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "recording"
)
data class RecordingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val title: String,
    val path: String,
    val duration: Long,
    val timestamp: Date,

    val manuscript: String = "",
)
