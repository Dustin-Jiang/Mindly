package top.tsukino.mindly.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.core.Role
import java.util.Date

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("conversationId")]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val conversationId: Long,

    var timestamp: Date,

    var text: String,
    var endReason: String? = null,

    val isUser: Boolean,
    val model: String? = null,
)

fun MessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        role = if (isUser) {
            Role.User
        } else {
            Role.Assistant
        },
        content = text,
    )
}