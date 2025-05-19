package top.tsukino.llmdemo.feature.settings

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.component.ModelSelectDialog
import top.tsukino.llmdemo.feature.common.component.ModelSelection

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun SettingsScreen(
    mainController: MainController
) {
    val vm: SettingsViewModel = hiltViewModel()

    val providers by vm.providersFlow.collectAsState()
    val models by vm.modelsFlow.collectAsState()
    val modelsSorted by remember(models) { 
        derivedStateOf { models.sortedBy { it.modelId } }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    val listState = rememberLazyListState()
    val expanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 || listState.firstVisibleItemScrollOffset < 0
        }
    }

    val showAddProviderDialog = remember { mutableStateOf(false) }
    val showEditProviderDialog = remember { mutableStateOf<ProviderEntity?>(null) }
    val showManageProviderSheet = remember { mutableStateOf<ProviderEntity?>(null) }

    val enableSummaryTitle by vm.enableSummaryTitle.collectAsState()
    val taskModelName by vm.taskModelName.collectAsState()
    val showTaskModelDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TitleBar(
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding()
            )) {
            LazyColumn {
                item(key = "ProviderLabel") {
                    Text(
                        text = "模型提供商",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                if (providers.isEmpty()) {
                    item("EmptyProvider") {
                        Text(
                            text = "没有可用的模型提供商",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                } else {
                    items(providers, key = { it.id }) { provider ->
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(
                                    Unit
                                ) {
                                    detectTapGestures(
                                        onLongPress = {
                                            showManageProviderSheet.value = provider
                                        },
                                        onTap = {
                                            showEditProviderDialog.value = provider
                                        }
                                    )
                                }
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        ) {
                            Text(provider.name, style = MaterialTheme.typography.labelLarge)
                            Text(
                                text = provider.host,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                item(key = "AddProvider") {
                    Button(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onClick = { showAddProviderDialog.value = true },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Provider"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "添加模型提供商"
                        )
                    }
                }

                item(key = "TaskModelLabel") {
                    Text(
                        text = "任务模型",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                item(key = "SummaryModelNameValue") {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(
                                Unit
                            ) {
                                detectTapGestures(
                                    onTap = {
                                        showTaskModelDialog.value = true
                                    }
                                )
                            }
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                    ) {
                        Text("选择任务模型", style = MaterialTheme.typography.labelLarge)
                        Text(
                            text = taskModelName.run {
                                if (this.isEmpty()) {
                                    "未选择"
                                } else {
                                    this
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (taskModelName.isNotEmpty()) {
                    item(
                        key = "ConversationExperienceLabel"
                    ) {
                        Text(
                            text = "对话体验",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                    item(
                        key = "SummaryModelSwitch"
                    ) {
                        Row() {
                            Box(
                                modifier = Modifier.align(
                                    Alignment.CenterVertically
                                )
                            ) {
                                Text(
                                    text = "使用任务模型总结对话标题",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Switch(
                                    modifier = Modifier.align(
                                        Alignment.CenterEnd
                                    )
                                        .padding(
                                            horizontal = 16.dp
                                        ),
                                    checked = enableSummaryTitle,
                                    onCheckedChange = {
                                        vm.updateEnableSummaryTitle(
                                            it
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                // 模型列表部分
                item(key = "ModelsLabel") {
                    Text(
                        text = "模型列表",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                if (models.isEmpty()) {
                    item("EmptyModels") {
                        Text(
                            text = "没有可用的模型",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                } else {
                    Log.d("SettingsScreen", "models: $models")
                    items(
                        items = modelsSorted,
                        key = { model -> "${model.id}" }
                    ) { model ->
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        ) {
                            Text(model.modelId, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }

    when {
        showAddProviderDialog.value -> {
            ProviderDialog(
                title = { Text("添加模型提供商") },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a provider") },
                onConfirm = {
                    showAddProviderDialog.value = false
                    vm.addProvider(it)
                },
                onDismiss = {
                    showAddProviderDialog.value = false
                }
            )
        }
        showEditProviderDialog.value != null -> {
            ProviderDialog(
                target = showEditProviderDialog.value!!,
                title = { Text("修改模型提供商") },
                icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit a provider") },
                onConfirm = {
                    vm.addProvider(it)
                    showEditProviderDialog.value = null
                },
                onDismiss = {
                    showEditProviderDialog.value = null
                },
                onTestConnection = {
                    vm.getProviderModels(it)
                }
            )
        }
        showManageProviderSheet.value != null -> {
            ProviderManageSheet(
                provider = showManageProviderSheet.value!!,
                onDismiss = {
                    showManageProviderSheet.value = null
                },
                onDelete = { provider ->
                    vm.deleteProvider(provider)
                    showManageProviderSheet.value = null
                }
            )
        }
        showTaskModelDialog.value -> {
            ModelSelectDialog(
                modelList = models,
                selected = models.find {
                    it.modelId == vm.taskModelName.value
                },
                onSelect = {
                    Log.d("TaskModelSelect", "Model selected: $it")
                    vm.updateTaskModelId(it.modelId)
                    showTaskModelDialog.value = false
                },
                onDismiss = {
                    showTaskModelDialog.value = false
                }
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ProviderManageSheet(
    provider: ProviderEntity,
    onDismiss: () -> Unit,
    onDelete: (ProviderEntity) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "管理模型提供商",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Button(
                onClick = {
                    onDelete(provider)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "删除模型提供商")
            }
        }
    }
}
