package top.tsukino.llmdemo.feature.chat

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.component.TopBar

@Composable
fun ChatScreen(
    mainController: MainController,
    conversationId: Long
) {
    val vm : ChatViewModel = hiltViewModel()

    val conversationState by vm.conversationState.collectAsState()
    val inputValueState by vm.inputFlow.collectAsState()

    val providerListState by vm.providerFlow.collectAsState()

    val modelListState by vm.modelListFlow.collectAsState()
    val modelState by vm.modelFlow.collectAsState()

    val scrollScope = rememberCoroutineScope()
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        vm.loadProviders()
        vm.loadModels()
        vm.loadConversation(conversationId)
        vm.loadSummaryTitle()
    }

    LaunchedEffect(conversationState?.messages?.size) {
        scrollToBottom(lazyColumnState, scrollScope, true)
    }

    LaunchedEffect(
        (modelListState.size.toString() + (conversationState?.conversation?.selectedModel?: ""))
    ) {
        modelListState.find {
            val selected = conversationState?.conversation?.selectedModel ?: ""
            it.modelId == selected
        }?.let {
            vm.selectModel(it)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = conversationState?.conversation?.title ?: "",
                onBack = { mainController.popBackStack() }
            )
        },
        bottomBar = {
            InputBar(
                inputValue = inputValueState,
                onInputValueUpdate = { vm.updateInputData(it) },
                onSend = {
                    vm.addUserMessage(inputValueState, {
                        scrollToBottom(lazyColumnState, scrollScope, true)
                    })
                    vm.clearInputData()
                },
                modelList = modelListState,
                selectedModel = modelState,
                onSelectModel = { model ->
                    vm.selectModel(model)
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            conversationState?.let {
                MessageList(
                    mainController,
                    conversation = it,
                    state = lazyColumnState
                )
            }
        }
    }
}

fun scrollToBottom(
    lazyListState: LazyListState,
    scrollScope: CoroutineScope,
    animate: Boolean = false
) {
    scrollScope.launch {
        coroutineScope {
            with(lazyListState) {
                if (animate)
                    animateScrollToItem(0)
                else
                    scrollToItem(0)
            }
        }
    }
}