package top.tsukino.mindly.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "conversation")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val title: String,
    val timestamp: Date,
    var selectedModel: String? = null,
)