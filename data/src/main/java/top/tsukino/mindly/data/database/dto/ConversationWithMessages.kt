package top.tsukino.mindly.data.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import top.tsukino.mindly.data.database.entity.ConversationEntity
import top.tsukino.mindly.data.database.entity.MessageEntity

data class ConversationWithMessages(
    @Embedded val conversation: ConversationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversationId",
        entity = MessageEntity::class
    )
    val messages: List<MessageEntity>
)