package top.tsukino.llmdemo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "collection_category"
)
data class CollectionCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
)