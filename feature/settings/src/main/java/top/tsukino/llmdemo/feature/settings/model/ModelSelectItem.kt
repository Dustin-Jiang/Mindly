package top.tsukino.llmdemo.feature.settings.model

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.feature.settings.SettingsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.StateFlow
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.feature.common.component.ModelSelectDialog

@Composable
internal fun ModelSelectItem(
    models: List<ModelEntity>,
    selectTitle: String,
    model: StateFlow<String>,
    onSelect: (ModelEntity) -> Unit,
    sort: Boolean = true,
    caption: (@Composable () -> Unit)? = null
) {
    val showModelSelectDialog = remember { mutableStateOf(false) }
    val modelList = if (sort) {
        models.sortedBy { it.modelId }
    } else {
        models
    }

    val selected by model.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(
                Unit
            ) {
                detectTapGestures(
                    onTap = {
                        showModelSelectDialog.value = true
                    }
                )
            }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Text(selectTitle, style = MaterialTheme.typography.labelLarge)
        caption?.invoke()
        Text(
            text = selected.run {
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

    when {
        showModelSelectDialog.value -> {
            ModelSelectDialog(
                modelList = modelList,
                selected = models.find {
                    it.modelId == selected
                },
                onSelect = {
                    Log.d("ModelSelect", "Model selected: $it")
                    showModelSelectDialog.value = false
                    onSelect(it)
                },
                onDismiss = {
                    showModelSelectDialog.value = false
                }
            )
        }
    }
}