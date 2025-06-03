package top.tsukino.llmdemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import top.tsukino.llmdemo.data.database.dao.CollectionTextDao
import top.tsukino.llmdemo.data.database.dao.ConversationDao
import top.tsukino.llmdemo.data.database.dao.MessageDao
import top.tsukino.llmdemo.data.database.dao.ModelDao
import top.tsukino.llmdemo.data.database.dao.ProviderDao
import top.tsukino.llmdemo.data.database.dao.RecordingDao
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity
import top.tsukino.llmdemo.data.database.entity.ConversationEntity
import top.tsukino.llmdemo.data.database.entity.MessageEntity
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.data.database.entity.RecordingEntity

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        ProviderEntity::class,
        ModelEntity::class,
        RecordingEntity::class,
        CollectionTextEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LLMDemoDatabase: RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun providerDao(): ProviderDao
    abstract fun modelDao(): ModelDao
    abstract fun recordingDao(): RecordingDao
    abstract fun collectionTextDao(): CollectionTextDao
}