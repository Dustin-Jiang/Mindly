package top.tsukino.mindly.feature.collect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.moderation.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.tsukino.mindly.config.MindlyPreferences
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.data.repo.base.CollectionCategoryRepo
import top.tsukino.mindly.data.repo.base.CollectionTextRepo
import top.tsukino.mindly.feature.common.helper.withScope
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class CreateTextItemViewModel @Inject constructor(
    private val collectionTextRepo: CollectionTextRepo,
    private val collectionCategoryRepo: CollectionCategoryRepo,
): ViewModel() {
    private val _categories = MutableStateFlow<List<CollectionCategoryEntity>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        withScope {
            collectionCategoryRepo.getCategoryList().collect {
                _categories.value = it
            }
        }
    }

    internal fun createCategory(
        name: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = CollectionCategoryEntity(name = name)
            collectionCategoryRepo.insertCategory(item)
        }
    }

    internal fun createTextItem(
        item: CollectionTextEntity
    ) {
        withScope {
            val id = collectionTextRepo.insertCollectionText(item)
        }
    }
}