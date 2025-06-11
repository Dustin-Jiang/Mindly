package top.tsukino.mindly.feature.common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.ModelEntity

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ModelSelectDialog(
    selected: ModelEntity?,
    modelList: List<ModelEntity>,
    onDismiss: () -> Unit,
    onSelect: (ModelEntity) -> Unit,
) {
    val selection = remember { mutableStateOf<ModelEntity?>(selected) }

    AlertDialog(
        modifier = Modifier.imePadding(),
        onDismissRequest = onDismiss,
        title = { Text("选择模型") },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
        confirmButton = {
            TextButton(
                onClick = {
                    selection.value?.let { onSelect(it) }
                },
                enabled = selection.value != null
            ) { Text("选择") }
        },
        text = {
            ModelSelectForm(
                modelList = modelList,
                selected = selection.value,
                onSelect = { model -> selection.value = model },
            )
        },
    )
}

@Composable
fun ModelSelectForm(
    modelList: List<ModelEntity>,
    selected: ModelEntity?,
    onSelect: (ModelEntity) -> Unit,
) {
    val displayItems = remember { mutableStateOf<List<ModelEntity>>(modelList) }
    val filterText = remember { mutableStateOf<String?>(null) }

    LazyColumn {
        item(key = "filter") {
            OutlinedTextField(
                value = filterText.value ?: "",
                onValueChange = { text ->
                    filterText.value = text
                    displayItems.value = modelList.filter {
                        it.modelId.contains(text, ignoreCase = true)
                    }
                },
                label = { Text("搜索") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        items(items = displayItems.value, key = { it.id }) { model ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selected?.id == model.id),
                        onClick = { onSelect(model) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected?.id == model.id,
                    onClick = null,
                )
                Text(
                    text = model.modelId,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        }
    }
}