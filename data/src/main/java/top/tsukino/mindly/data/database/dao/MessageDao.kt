package top.tsukino.mindly.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import top.tsukino.mindly.data.database.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE conversationId = :conversationId")
    suspend fun getMessagesByConversationId(conversationId: Long): List<MessageEntity>

    @Query("SELECT * FROM message WHERE id = :id")
    suspend fun getMessageById(id: Long): MessageEntity

    @Insert
    suspend fun addMessage(message: MessageEntity): Long

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("DELETE FROM message WHERE id = :id")
    suspend fun deleteMessageById(id: Long)

    @Query("DELETE FROM message WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversationId(conversationId: Long)

    @Query("UPDATE message SET text = :text WHERE id = :id")
    suspend fun updateMessage(id: Long, text: String)

    @Query("UPDATE message SET isUser = :isUser WHERE id = :id")
    suspend fun updateMessage(id: Long, isUser: Boolean)
}