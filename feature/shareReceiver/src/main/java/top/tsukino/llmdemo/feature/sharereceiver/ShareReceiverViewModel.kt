package top.tsukino.llmdemo.feature.sharereceiver

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import top.tsukino.llmdemo.data.database.entity.CollectionTextEntity
import top.tsukino.llmdemo.data.repo.base.CollectionTextRepo
import javax.inject.Inject

@HiltViewModel
class ShareReceiverViewModel @Inject constructor (
    private val collectionTextRepo: CollectionTextRepo,
): ViewModel() {
    suspend fun saveText(item: CollectionTextEntity) {
        collectionTextRepo.insertCollectionText(item)
    }
}