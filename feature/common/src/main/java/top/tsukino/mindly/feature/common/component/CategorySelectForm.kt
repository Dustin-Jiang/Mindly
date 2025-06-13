package top.tsukino.mindly.feature.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity

fun LazyListScope.CategorySelectForm(
    categories: List<CollectionCategoryEntity>,
    selected: Long? = null,
    onSelect: (Long?) -> Unit,
    onCreate: (String) -> Unit,
) {
    item(key = "no_category") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (selected == null),
                    onClick = { onSelect(null) },
                    role = Role.RadioButton
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected == null,
                onClick = null,
            )
            Text(
                text = "无分类",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
    items(items = categories, key = { it.id }) { item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (selected == item.id),
                    onClick = { onSelect(item.id) },
                    role = Role.RadioButton
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected == item.id,
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
        val showNewCategory = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { showNewCategory.value = true },
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
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

        when {
            showNewCategory.value -> {
                CreateCategoryDialog(
                    onDismiss = { showNewCategory.value = false },
                    onCreate = onCreate
                )
            }
        }
    }
}