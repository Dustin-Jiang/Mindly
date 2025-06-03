package top.tsukino.llmdemo.feature.sharereceiver

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.api.LLMDemoApi
import top.tsukino.llmdemo.config.LLMPreferences
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.data.repo.base.CollectionTextRepo
import top.tsukino.llmdemo.data.repo.base.ModelRepo
import top.tsukino.llmdemo.data.repo.base.ProviderRepo
import javax.inject.Inject

@HiltViewModel
class ShareReceiverViewModel @Inject constructor (
    private val collectionTextRepo: CollectionTextRepo,
    private val modelRepo: ModelRepo,
    private val providerRepo: ProviderRepo,
    private val preferences: LLMPreferences,
    private val api: LLMDemoApi,
): ViewModel() {
    suspend fun saveText(item: CollectionTextEntity) {
        collectionTextRepo.insertCollectionText(item)
    }
}