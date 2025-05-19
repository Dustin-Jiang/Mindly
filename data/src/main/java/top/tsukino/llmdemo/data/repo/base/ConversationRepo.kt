package top.tsukino.llmdemo.data.repo.base

import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import top.tsukino.llmdemo.data.database.dto.ConversationWithMessages
import top.tsukino.llmdemo.data.database.entity.ConversationEntity
import top.tsukino.llmdemo.data.database.entity.MessageEntity

interface ConversationRepo {
    suspend fun getConversation(
        id: Long
    ): Flow<ConversationWithMessages>

    suspend fun deleteConversation(
        id: Long
    )

    suspend fun getAllConversations(): Flow<List<ConversationEntity>>

    suspend fun createConversation(
        title: String
    ): Long

    @Transaction
    suspend fun addMessage(message: MessageEntity): Long

    @Transaction
    suspend fun deleteMessage(message: MessageEntity)

    @Transaction
    suspend fun updateMessage(message: MessageEntity)

    suspend fun updateSelectedModel(id: Long, model: String)

    suspend fun updateConversationTitle(id: Long, title: String)
}