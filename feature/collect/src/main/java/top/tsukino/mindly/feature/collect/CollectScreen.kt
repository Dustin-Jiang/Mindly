package top.tsukino.mindly.feature.collect

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.common.component.TitleBar
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import top.tsukino.mindly.feature.collect.items.CollectItem
import top.tsukino.mindly.feature.collect.items.RecordingItem
import top.tsukino.mindly.feature.collect.items.toItemId
import top.tsukino.mindly.feature.collect.items.RecordingItemManageSheet
import top.tsukino.mindly.feature.collect.items.TextItem
import top.tsukino.mindly.feature.collect.items.TextItemManageSheet
import top.tsukino.mindly.feature.common.component.ResultType
import top.tsukino.mindly.feature.common.component.ResultView
import top.tsukino.mindly.feature.common.component.audioplayer.AudioPlayerViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun CollectScreen(
    mainController: MainController,
    vm: CollectViewModel = hiltViewModel(),
    playerVm: AudioPlayerViewModel = hiltViewModel(),
) {
    vm.mainController = mainController
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    val listState = rememberLazyListState()
    val expanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 || listState.firstVisibleItemScrollOffset < 0
        }
    }

    val recordingList = vm.recordingList.collectAsState()
    val collectionTextList = vm.collectionTextList.collectAsState()
    val playerState by playerVm.playerState.collectAsState()
    val currentShowing by vm.currentShowing.collectAsState()
    val categories by vm.collectionCategoryList.collectAsState()
    val selectedCategory by vm.selectedCategory.collectAsState()

    val collectList = remember {
        derivedStateOf {
            val list = mutableListOf<CollectItem>()
            list.addAll(recordingList.value.map { item ->
                RecordingItem(
                    data = item,
                    id = item.toItemId(),
                    show = currentShowing == item.toItemId(),
                    playerState = playerState,
                    onShow = {
                        playerVm.reset()
                        if (currentShowing == item.toItemId()) {
                            vm.clearCurrentShowing()
                        }
                        else {
                            playerVm.setAudioFile(item.path)
                            vm.setCurrentShowing(item.toItemId())
                        }
                    },
                    onShowManageSheet = {
                        vm.showRecordingManageSheet(it)
                    },
                    onPlayClick = { playerVm.play() },
                    onPauseClick = { playerVm.pause() },
                    onSeek = { playerVm.seekTo(it) },
                    onSeekForward = { playerVm.seekBy(it) },
                    onSeekBackward = { playerVm.seekBy(-it) }
                )
            })

            list.addAll(collectionTextList.value.map { item ->
                TextItem(
                    data = item,
                    title = item.title,
                    show = currentShowing == item.toItemId(),
                    onShow = {
                        if (currentShowing == item.toItemId()) {
                            vm.clearCurrentShowing()
                        }
                        else {
                            vm.setCurrentShowing(item.toItemId())
                        }
                    },
                    onShowSheet = {
                        vm.showTextManageSheet(item.id)
                    },
                    id = item.toItemId()
                )
            })

            list.sortByDescending { it.timestamp }
            if (selectedCategory < 0) list
            else list.filter { item -> item.category == selectedCategory }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                TitleBar(
                    title = "收藏",
                    navigationIcon = null,
                    scrollBehavior = scrollBehavior
                )
                CategoryList(
                    categories = categories,
                    selected = selectedCategory,
                    onChange = { id ->
                        vm.setSelectedCategory(id)
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()
                )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (collectList.value.isEmpty()) {
                    Box(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        ResultView(
                            type = ResultType.EMPTY2,
                            text = "没有收藏",
                        )
                    }
                }
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            items = collectList.value,
                            key = { item ->
                                item.id.id
                            }
                        ) { item ->
                            item.Display(
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(
                    1000f
                )
        ) {
            CreateRecordButton(
                mainController = mainController,
                onStart = {
                    vm.startRecording()
                },
                onStop = {
                    vm.stopRecording()
                }
            )
        }
    }

    val enableTranscript by vm.enableTranscript
    val enableSummaryTitle by vm.enableSummaryTitle
    val activity = LocalActivity.current
    val showCategorySelectSheet by vm.showCategorySelectSheet.collectAsState()

    when {
        showCategorySelectSheet != null -> {
            val id =  showCategorySelectSheet
            collectList.value.find { id == it.id }?.let { item ->
                ModalBottomSheet(
                    modifier = Modifier.defaultMinSize(minHeight = 256.dp),
                    onDismissRequest = { vm.showCategorySelectSheet(null) }
                ) {
                    CategorySelectForm(
                        categories = categories,
                        selected = item.category,
                        onSubmit = { id ->
                            vm.updateItemCategory(item = item, id = id)
                            vm.showCategorySelectSheet(null)
                        },
                        onCreate = { title ->
                            vm.createCategory(title)
                        },
                        onDismiss = { vm.showCategorySelectSheet(null) },
                    )
                }
            }
        }
        vm.showRecordingManageSheet.collectAsState().value != null -> {
            RecordingItemManageSheet(
                id = vm.showRecordingManageSheet.collectAsState().value ?: 0L,
                onDismiss = { vm.showRecordingManageSheet(null) },
                onDelete = { id ->
                    vm.deleteRecording(id)
                    vm.showRecordingManageSheet(null)
                },
                isTranscriptEnabled = enableTranscript,
                onTranscript = { id ->
                    vm.transcriptRecording(id)
                    mainController.apply {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "正在转录语音"
                            )
                        }
                    }
                    vm.showRecordingManageSheet(null)
                },
                isSummaryTitleEnabled = enableSummaryTitle,
                onSummary = { id ->
                    try {
                        vm.recordingSummary(id)
                    }
                    catch (e: IllegalStateException) {
                        mainController.scope.launch {
                            mainController.snackbarHostState.showSnackbar(
                                message = "${e.message}"
                            )
                        }
                        return@RecordingItemManageSheet
                    }
                    catch (e: Exception) {
                        mainController.scope.launch {
                            mainController.snackbarHostState.showSnackbar(
                                message = "生成总结标题失败: ${e.message}"
                            )
                        }
                        Log.e("CollectScreen", "Error summarizing recording", e)
                        return@RecordingItemManageSheet
                    }
                    mainController.apply {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "正在生成总结标题"
                            )
                        }
                    }
                    vm.showRecordingManageSheet(null)
                },
                onCreateConversation = { id ->
                    val recording = vm.recordingList.value.find { it.id == id }
                    recording?.transcript?.let { text ->
                        vm.createConversation(text)
                        vm.showTextManageSheet(null)
                    }
                    vm.showRecordingManageSheet(null)
                },
                onShareTranscript = { id ->
                    val recording = vm.recordingList.value.find { it.id == id }
                    recording?.transcript?.let { text ->
                        vm.shareText(activity, text)
                        vm.showTextManageSheet(null)
                    }
                },
                onSelectCategory = { id ->
                    val item = vm.recordingList.value.find { it.id == id }
                    item?.let {
                        vm.showCategorySelectSheet(it.toItemId())
                    }
                },
            )
        }
        vm.showTextManageSheet.collectAsState().value != null -> {
            TextItemManageSheet(
                id = vm.showTextManageSheet.collectAsState().value ?: 0L,
                onDismiss = { vm.showTextManageSheet(null) },
                onDelete = { id ->
                    vm.deleteTextItem(id)
                },
                isSummaryTitleEnabled = enableSummaryTitle,
                onSummary = { id ->
                    try {
                        vm.textSummary(id)
                    }
                    catch (e: IllegalStateException) {
                        mainController.scope.launch {
                            mainController.snackbarHostState.showSnackbar(
                                message = "${e.message}"
                            )
                        }
                        return@TextItemManageSheet
                    }
                    catch (e: Exception) {
                        mainController.scope.launch {
                            mainController.snackbarHostState.showSnackbar(
                                message = "生成总结标题失败: ${e.message}"
                            )
                        }
                        Log.e("CollectScreen", "Error summarizing text", e)
                        return@TextItemManageSheet
                    }
                    mainController.apply {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "正在生成总结标题"
                            )
                        }
                    }
                    vm.showTextManageSheet(null)
                },
                onCreateConversation = { id ->
                    val text = vm.collectionTextList.value.find { it.id == id }
                    text?.content?.let { text ->
                        vm.createConversation(text)
                        vm.showTextManageSheet(null)
                    }
                    vm.showRecordingManageSheet(null)
                },
                onShare = { id ->
                    val text = vm.collectionTextList.value.find { it.id == id }
                    text?.content?.let { text ->
                        vm.shareText(activity, text)
                        vm.showTextManageSheet(null)
                    }
                },
                onSelectCategory = { id ->
                    val item = vm.collectionTextList.value.find { it.id == id }
                    item?.let {
                        vm.showCategorySelectSheet(it.toItemId())
                    }
                },
            )
        }
    }
}