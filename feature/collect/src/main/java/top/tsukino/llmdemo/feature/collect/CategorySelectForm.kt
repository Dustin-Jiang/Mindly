package top.tsukino.llmdemo.feature.collect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import top.tsukino.llmdemo.data.database.entity.CollectionCategoryEntity

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CategorySelectForm(
    categories: List<CollectionCategoryEntity>,
    selected: Long? = null,
    onSubmit: (Long) -> Unit,
    onCreate: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val selected = remember { mutableStateOf(selected) }
    val showNewCategory = remember { mutableStateOf(false) }

    LazyColumn {
        item(key = "heading") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                    )
                }
                IconButton(
                    enabled = selected.value != null,
                    onClick = { selected.value?.let { onSubmit(it) } }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Confirm",
                    )
                }
            }
        }
        item(key = "title") {
            Text(
                text = "选择分类",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }
        items(items = categories, key = { it.id }) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selected.value == item.id),
                        onClick = { selected.value = item.id },
                        role = Role.RadioButton
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selected.value == item.id,
                    onClick = null,
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        }
        item(key = "new_category") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { showNewCategory.value = true },
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Category",
                )
                Text(
                    text = "新建分类",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        }
    }

    when {
        showNewCategory.value -> {
            val onDismiss = { showNewCategory.value = false }
            val title = remember { mutableStateOf("") }
            AlertDialog(
                modifier = Modifier.imePadding(),
                onDismissRequest = onDismiss,
                title = { Text("新建分类") },
                dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (title.value.isNotEmpty()) { onCreate(title.value) }
                        },
                        enabled = title.value.isNotEmpty()
                    ) { Text("选择") }
                },
                text = {
                    OutlinedTextField(
                        value = title.value,
                        onValueChange = { text ->
                            title.value = text
                        },
                        label = { Text("标题") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
            )
        }
    }
}