package top.tsukino.mindly.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.settings.model.ModelSettings
import top.tsukino.mindly.feature.settings.model.ModelsList
import top.tsukino.mindly.feature.settings.provider.ProviderSettings

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
                ProviderSettings(vm = vm, providers = providers)
                ModelSettings(vm = vm, models = models)
                ModelsList(vm = vm, models = modelsSorted)
            }
        }
    }
}
