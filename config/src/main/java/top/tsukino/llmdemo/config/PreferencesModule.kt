package top.tsukino.llmdemo.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class PreferencesModule {
    @Provides
    @Singleton
    fun providePreferences(
        @ApplicationContext context: Context
    ): LLMPreferences {
        return LLMPreferences(context)
    }
}