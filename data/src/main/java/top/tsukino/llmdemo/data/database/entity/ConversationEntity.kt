package top.tsukino.llmdemo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import top.tsukino.llmdemo.data.database.Converters
import java.util.Date

@Entity(tableName = "conversation")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val title: String,
    val timestamp: Date,
    var selectedModel: String? = null,
)