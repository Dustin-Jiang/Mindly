package top.tsukino.mindly.feature.sharereceiver

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ShareReceiverScreen(
    shareText: String? = null,
) {
    val vm = hiltViewModel<ShareReceiverViewModel>()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    val scrollState = rememberScrollState()

    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()

    val onSave: suspend (item: CollectionItemDisplay<*>) -> Unit = { item ->
        try {
            when {
                item is TextContents -> {
                    vm.saveText(item.toEntity())
                }
                else -> {
                }
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(activity?.applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            activity?.finish()
        }
    }
    val item: CollectionItemDisplay<*>? = when {
        shareText != null -> {
            TextContents(
                text = shareText
            )
        }

        else -> { null }
    }

    if (item == null) {
        activity?.finish()
    }

    item?.let { item ->
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ShareReceiverTitleBar(
                    onBackClick = {
                        activity?.finish()
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    item.Display()
                }

                ShareReceiverFloatingActionButton(
                    onSave = {
                        scope.launch(Dispatchers.IO) {
                            onSave(item)
                        }
                    }
                )
            }
        }
    }
}
