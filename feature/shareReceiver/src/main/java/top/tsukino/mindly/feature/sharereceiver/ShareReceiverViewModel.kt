package top.tsukino.mindly.feature.sharereceiver

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import top.tsukino.mindly.api.MindlyApi
import top.tsukino.mindly.config.MindlyPreferences
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.data.repo.base.CollectionTextRepo
import top.tsukino.mindly.data.repo.base.ModelRepo
import top.tsukino.mindly.data.repo.base.ProviderRepo
import javax.inject.Inject

@HiltViewModel
class ShareReceiverViewModel @Inject constructor (
    private val collectionTextRepo: CollectionTextRepo,
    private val modelRepo: ModelRepo,
    private val providerRepo: ProviderRepo,
    private val preferences: MindlyPreferences,
    private val api: MindlyApi,
): ViewModel() {
    suspend fun saveText(item: CollectionTextEntity) {
        collectionTextRepo.insertCollectionText(item)
    }
}