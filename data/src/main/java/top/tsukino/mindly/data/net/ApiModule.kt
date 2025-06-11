package top.tsukino.mindly.data.net

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import top.tsukino.mindly.api.LLMDemoApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApiModule {
    private val api = LLMDemoApi()

    @Provides
    @Singleton
    fun provideApi(
        @ApplicationContext context: Context
    ): LLMDemoApi {
        return api
    }
}