package top.tsukino.llmdemo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "collection_text"
)
data class CollectionTextEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,
    val content: String,
    val timestamp: Date,
)