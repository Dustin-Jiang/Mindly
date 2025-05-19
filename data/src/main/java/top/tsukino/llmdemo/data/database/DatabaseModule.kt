package top.tsukino.llmdemo.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val FILE_NAME = "data.db"

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LLMDemoDatabase {
        return Room.databaseBuilder(
            context,
            LLMDemoDatabase::class.java,
            FILE_NAME
        ).build()
    }
}