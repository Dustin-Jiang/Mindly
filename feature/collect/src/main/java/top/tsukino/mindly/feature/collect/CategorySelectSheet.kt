package top.tsukino.mindly.feature.collect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity
import top.tsukino.mindly.feature.common.component.CategorySelectForm

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