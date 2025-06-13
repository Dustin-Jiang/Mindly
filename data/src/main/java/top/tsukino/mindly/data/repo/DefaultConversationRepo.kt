package top.tsukino.mindly.data.repo

import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.api.MindlyApi
import top.tsukino.mindly.data.database.MindlyDatabase
import top.tsukino.mindly.data.database.dto.ConversationWithMessages
import top.tsukino.mindly.data.database.entity.ConversationEntity
import top.tsukino.mindly.data.database.entity.MessageEntity
import top.tsukino.mindly.data.repo.base.ConversationRepo
import java.util.Date
import javax.inject.Inject

class DefaultConversationRepo @Inject constructor(
    private val database: MindlyDatabase,
    private val api: MindlyApi
): ConversationRepo {
    override suspend fun getConversation(
        id: Long
    ): Flow<ConversationWithMessages> {
        return database.conversationDao().getConversationById(id)
    }

    override suspend fun deleteConversation(
        id: Long
    ) {
        database.conversationDao().deleteConversationById(id)
    }

    override suspend fun getAllConversations(): Flow<List<ConversationEntity>> {
        return database.conversationDao().getAllConversations()
    }

    override suspend fun createConversation(
        title: String
    ): Long {
        val conversation = ConversationEntity(
            title = title,
            timestamp = Date(),
            id = 0L,
        )
        return database.conversationDao().createConversation(conversation)
    }

    @Transaction
    override suspend fun addMessage(message: MessageEntity): Long {
        val id = database.messageDao().addMessage(message)
        database.conversationDao().updateTimestamp(message.conversationId, message.timestamp)
        return id
    }

    @Transaction
    override suspend fun updateMessage(message: MessageEntity) {
        database.messageDao().updateMessage(message)
        database.conversationDao().updateTimestamp(message.conversationId, message.timestamp)
    }

    @Transaction
    override suspend fun deleteMessage(message: MessageEntity) {
        database.messageDao().deleteMessage(message)
        database.conversationDao().updateTimestamp(message.conversationId, Date())
    }

    override suspend fun updateSelectedModel(id: Long, model: String) {
        database.conversationDao().updateSelectedModel(id, model)
    }

    override suspend fun updateConversationTitle(id: Long, title: String) {
        database.conversationDao().updateTitle(id, title)
    }
}