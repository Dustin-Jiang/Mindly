package top.tsukino.llmdemo.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.data.database.entity.ConversationEntity
import top.tsukino.llmdemo.data.repo.base.ConversationRepo
import top.tsukino.llmdemo.feature.common.helper.withScope
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversationRepo: ConversationRepo
) : ViewModel() {
    private val _conversations = MutableStateFlow<List<ConversationEntity>>(emptyList())
    val conversations: StateFlow<List<ConversationEntity>> = _conversations.asStateFlow()

    private var job: Job? = null

    init {
        Log.d("HomeViewModel", "init")
        withScope {
            getAllConversations()
        }
    }

    private fun getAllConversations() {
        job?.cancel()
        job = viewModelScope.launch {
            conversationRepo.getAllConversations().collect(_conversations::emit)
        }
    }

    fun createConversation(
        onCreated: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val newId = conversationRepo.createConversation("新对话")
            onCreated(newId)
        }
    }
}