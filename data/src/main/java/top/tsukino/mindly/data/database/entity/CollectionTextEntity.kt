package top.tsukino.mindly.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "collection_text",
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
        Index("category"),
    ]
)
data class CollectionTextEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,
    val content: String,
    val timestamp: Date,

    val category: Long? = null,
)