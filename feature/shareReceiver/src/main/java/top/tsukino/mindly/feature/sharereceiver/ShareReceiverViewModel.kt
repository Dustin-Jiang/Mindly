package top.tsukino.mindly.feature.sharereceiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.tsukino.mindly.api.MindlyApi
import top.tsukino.mindly.config.MindlyPreferences
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.data.repo.base.CollectionCategoryRepo
import top.tsukino.mindly.data.repo.base.CollectionTextRepo
import top.tsukino.mindly.data.repo.base.ModelRepo
import top.tsukino.mindly.data.repo.base.ProviderRepo
import top.tsukino.mindly.feature.common.helper.withScope
import javax.inject.Inject

@HiltViewModel
class ShareReceiverViewModel @Inject constructor (
    private val collectionTextRepo: CollectionTextRepo,
    private val modelRepo: ModelRepo,
    private val providerRepo: ProviderRepo,
    private val collectionCategoryRepo: CollectionCategoryRepo,
    private val preferences: MindlyPreferences,
    private val api: MindlyApi,
): ViewModel() {
    init {
        withScope {
            collectionCategoryRepo.getCategoryList().collect {
                _categoryList.value = it
            }
        }
    }

    suspend fun saveText(item: CollectionTextEntity) {
        collectionTextRepo.insertCollectionText(item)
    }

    private val _categoryList = MutableStateFlow<List<CollectionCategoryEntity>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    internal fun createCategory(
        name: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = CollectionCategoryEntity(name = name)
            collectionCategoryRepo.insertCategory(item)
        }
    }
}