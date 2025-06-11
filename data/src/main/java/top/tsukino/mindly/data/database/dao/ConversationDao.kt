package top.tsukino.mindly.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import top.tsukino.mindly.data.database.dto.ConversationWithMessages
import top.tsukino.mindly.data.database.entity.ConversationEntity
import java.util.Date

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversation")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Transaction
    @Query("SELECT * FROM conversation WHERE id = :id")
    fun getConversationById(id: Long): Flow<ConversationWithMessages>

    @Transaction
    @Query("DELETE FROM conversation WHERE id = :id")
    suspend fun deleteConversationById(id: Long)

    @Insert
    suspend fun createConversation(item: ConversationEntity): Long

    @Query("UPDATE conversation SET title = :title WHERE id = :id")
    suspend fun updateConversation(title: String, id: Long)

    @Query("UPDATE conversation SET timestamp = :timestamp WHERE id = :id")
    suspend fun updateTimestamp(id: Long, timestamp: Date)

    @Query("UPDATE conversation SET selectedModel = :model WHERE id = :id")
    suspend fun updateSelectedModel(id: Long, model: String)

    @Query("UPDATE conversation SET title = :title WHERE id = :id")
    suspend fun updateTitle(id: Long, title: String)
}