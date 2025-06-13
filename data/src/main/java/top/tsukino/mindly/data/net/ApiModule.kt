package top.tsukino.mindly.data.net

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import top.tsukino.mindly.api.MindlyApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApiModule {
    private val api = MindlyApi()

    @Provides
    @Singleton
    fun provideApi(
        @ApplicationContext context: Context
    ): MindlyApi {
        return api
    }
}