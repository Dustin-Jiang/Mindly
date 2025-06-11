package top.tsukino.mindly.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import top.tsukino.mindly.data.database.dao.CollectionCategoryDao
import top.tsukino.mindly.data.database.dao.CollectionTextDao
import top.tsukino.mindly.data.database.dao.ConversationDao
import top.tsukino.mindly.data.database.dao.MessageDao
import top.tsukino.mindly.data.database.dao.ModelDao
import top.tsukino.mindly.data.database.dao.ProviderDao
import top.tsukino.mindly.data.database.dao.RecordingDao
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.data.database.entity.ConversationEntity
import top.tsukino.mindly.data.database.entity.MessageEntity
import top.tsukino.mindly.data.database.entity.ModelEntity
import top.tsukino.mindly.data.database.entity.ProviderEntity
import top.tsukino.mindly.data.database.entity.RecordingEntity

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        ProviderEntity::class,
        ModelEntity::class,
        RecordingEntity::class,
        CollectionTextEntity::class,
        CollectionCategoryEntity::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MindlyDatabase: RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun providerDao(): ProviderDao
    abstract fun modelDao(): ModelDao
    abstract fun recordingDao(): RecordingDao
    abstract fun collectionTextDao(): CollectionTextDao
    abstract fun collectionCategoryDao(): CollectionCategoryDao
}