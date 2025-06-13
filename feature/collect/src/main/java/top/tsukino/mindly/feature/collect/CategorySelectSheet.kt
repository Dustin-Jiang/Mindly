package top.tsukino.mindly.feature.collect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CategorySelectSheet(
    categories: List<CollectionCategoryEntity>,
    selected: Long? = null,
    onSubmit: (Long) -> Unit,
    onCreate: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val selected = remember { mutableStateOf(selected) }

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
        CategorySelectForm(
            categories = categories,
            selected = selected.value,
            onSelect = { selected.value = it },
            onCreate = onCreate
        )
    }
}