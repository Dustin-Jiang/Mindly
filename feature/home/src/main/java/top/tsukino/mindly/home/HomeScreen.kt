package top.tsukino.mindly.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.common.NavDest
import top.tsukino.mindly.feature.common.component.ResultType
import top.tsukino.mindly.feature.common.component.ResultView

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    mainController: MainController,
    navController: NavController,
    vm : HomeViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    val listState = rememberLazyListState()
    val expanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 || listState.firstVisibleItemScrollOffset < 0
        }
    }

    val conversations = vm.conversations.collectAsState()
    val conversationsSorted = remember {
        derivedStateOf {
            conversations.value.sortedByDescending { it.timestamp }
        }
    }

    val showConversationManageSheet = remember { mutableStateOf(-1L) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TitleBar(
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(
                top = innerPadding.calculateTopPadding(),
            )
        ) {
            if (conversationsSorted.value.isEmpty()) {
                Box(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    ResultView(
                        type = ResultType.EMPTY,
                        text = "没有对话",
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(conversationsSorted.value) { item ->
                        ConversationItem(
                            mainController,
                            item,
                            onShowSheet = { showConversationManageSheet.value = it },
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
            ) {
                ExtendedFloatingActionButton(
                    expanded = expanded,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        vm.createConversation({ id ->
                            mainController.navigate(NavDest.Chat(id))
                        })
                    },
                    text = {
                        Text(
                            text = "新建对话"
                        )
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            "新建对话"
                        )
                    }
                )
            }
        }
    }

    when {
        showConversationManageSheet.value != -1L -> {
            ConversationManageSheet(
                vm = vm,
                id = showConversationManageSheet.value,
                onDismiss = {
                    showConversationManageSheet.value = -1L
                },
                onDelete = { id ->
                    vm.deleteConversation(id)
                    showConversationManageSheet.value = -1L
                }
            )
        }
    }
}