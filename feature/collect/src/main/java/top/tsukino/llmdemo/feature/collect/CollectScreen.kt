package top.tsukino.llmdemo.feature.collect

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.component.TitleBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.feature.collect.items.CollectItem
import top.tsukino.llmdemo.feature.collect.items.RecordingItem
import top.tsukino.llmdemo.feature.collect.items.toItemId
import top.tsukino.llmdemo.feature.collect.items.RecordingItemManageSheet
import top.tsukino.llmdemo.feature.common.component.ResultType
import top.tsukino.llmdemo.feature.common.component.ResultView
import top.tsukino.llmdemo.feature.common.component.audioplayer.AudioPlayerViewModel

@OptIn(
    ExperimentalMaterial3Api::class
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

    val recordingList = vm.recordingList.collectAsState()
    val playerState by playerVm.playerState.collectAsState()
    val currentShowing by vm.currentShowing.collectAsState()

    val collectList = remember {
        derivedStateOf {
            val list = mutableListOf<CollectItem>()
            list.addAll(recordingList.value.map { item ->
                RecordingItem(
                    data = item,
                    id = item.toItemId(),
                    show = currentShowing == item.toItemId(),
                    playerState = playerState,
                    onShow = { path ->
                        playerVm.reset()
                        if (currentShowing == item.toItemId()) {
                            vm.clearCurrentShowing()
                        }
                        else {
                            playerVm.setAudioFile(path)
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

            list.sortByDescending { it.timestamp }
            list
        }
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = "收藏",
                navigationIcon = null,
                scrollBehavior = scrollBehavior
            )
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
                        items(items = collectList.value) { item ->
                            item.Display()
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

    when {
        vm.showRecordingManageSheet.collectAsState().value != null -> {
            RecordingItemManageSheet(
                id = vm.showRecordingManageSheet.collectAsState().value ?: 0L,
                onDismiss = { vm.showRecordingManageSheet(null) },
                onDelete = { id ->
                    vm.deleteRecording(id)
                    vm.showRecordingManageSheet(null)
                },
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
                }
            )
        }
    }
}