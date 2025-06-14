package top.tsukino.mindly.feature.collect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.common.component.TitleBar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTextItemScreen(
    mainController: MainController
) {
    val vm: CreateTextItemViewModel = hiltViewModel()

    val categories by vm.categories.collectAsState()
    val content = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("文本收集") }
    val selectedCategoryState = remember { mutableStateOf<Long?>(null) }

    val onDismiss: () -> Unit = {
        mainController.popBackStack()
    }

    val onSubmit: (CollectionTextEntity) -> Unit = {
        vm.createTextItem(it)
        onDismiss()
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TitleBar(
                title = "新建文本收集",
                navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
                onNavigation = onDismiss,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = mainController.snackbarHostState
            )
        }
    ) { paddingValues ->
        CreateTextItemForm(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            categories = categories,
            content = content.value,
            title = title.value,
            selectedCategory = selectedCategoryState.value,
            updateContent = { content.value = it },
            updateTitle = { title.value = it },
            updateSelectedCategory = { selectedCategoryState.value = it }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                ExtendedFloatingActionButton(
                    expanded = true,
                    onClick = {
                        if (content.value.isEmpty()) {
                            mainController.run {
                                scope.launch {
                                    snackbarHostState.showSnackbar("内容不能为空")
                                }
                            }
                            return@ExtendedFloatingActionButton
                        }
                        if (title.value.isEmpty()) {
                            mainController.run {
                                scope.launch {
                                    snackbarHostState.showSnackbar("标题不能为空")
                                }
                            }
                            return@ExtendedFloatingActionButton
                        }
                        onSubmit(
                            CollectionTextEntity(
                                content = content.value,
                                title = title.value,
                                timestamp = Date(),
                                category = selectedCategoryState.value,
                            )
                        )
                    },
                    text = {
                        Text("保存")
                    },
                    icon = {
                        Icon(Icons.Default.Save, contentDescription = "保存")
                    }
                )
            }
        }
    }
}