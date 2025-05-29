package top.tsukino.llmdemo.feature.collect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.component.TitleBar

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CollectScreen(
    mainController: MainController,
    vm: CollectViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

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
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(
                    top = innerPadding.calculateTopPadding(),
                )
            ) {
                Text("Test Collections")
                Box(
                    modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
                ) {
                    CreateRecordButton(
                        onStart = {},
                        onStop = {}
                    )
                }
            }
        }
    }

}