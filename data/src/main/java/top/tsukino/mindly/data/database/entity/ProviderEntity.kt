package top.tsukino.mindly.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provider")
data class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val host: String,
    val token: String,
    val isEnabled: Boolean = true,
)