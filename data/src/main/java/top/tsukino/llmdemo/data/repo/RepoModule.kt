package top.tsukino.llmdemo.data.repo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import top.tsukino.llmdemo.data.repo.base.ConversationRepo
import top.tsukino.llmdemo.data.repo.base.ModelRepo
import top.tsukino.llmdemo.data.repo.base.ProviderRepo
import top.tsukino.llmdemo.data.repo.base.RecordingRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Singleton
    @Binds
    abstract fun bindConversationRepo(
        conversationRepo: DefaultConversationRepo
    ) : ConversationRepo

    @Singleton
    @Binds
    abstract fun bindProviderRepo(
        providerRepo: DefaultProviderRepo
    ) : ProviderRepo

    @Singleton
    @Binds
    abstract fun bindModelRepo(
        modelRepo: DefaultModelRepo
    ) : ModelRepo

    @Singleton
    @Binds
    abstract fun bindRecordingRepo(
        recordingRepo: DefaultRecordingRepo
    ) : RecordingRepo
}