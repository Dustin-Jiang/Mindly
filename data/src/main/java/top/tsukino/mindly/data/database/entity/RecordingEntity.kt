package top.tsukino.mindly.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "recording",
    foreignKeys = [
        ForeignKey(
            entity = CollectionCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index("category")
    ]
)
data class RecordingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val title: String,
    val path: String,
    val duration: Long,
    val timestamp: Date,

    val transcript: String = "",

    val category: Long? = null,
)
