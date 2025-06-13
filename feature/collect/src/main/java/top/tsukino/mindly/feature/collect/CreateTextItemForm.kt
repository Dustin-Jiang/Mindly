package top.tsukino.mindly.feature.collect

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.feature.common.component.CategorySelectForm
import top.tsukino.mindly.feature.common.component.SheetLabel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTextItemForm(
    onDismiss: () -> Unit,
    onSubmit: (CollectionTextEntity) -> Unit,
    categories: List<CollectionCategoryEntity>,
    selectedCategory: Long? = null,
) {
    val content = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("文本收集") }
    val selectedCategoryState = remember { mutableStateOf(selectedCategory) }

    if (selectedCategory == null || selectedCategory < 0L) {
        selectedCategoryState.value = null
    }

    Log.d("CreateTextItemForm", "Selected Category: ${selectedCategoryState.value}")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onDismiss,
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                    Button(
                        enabled = content.value.isNotEmpty(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            val entity = CollectionTextEntity(
                                id = 0,
                                content = content.value,
                                title = title.value,
                                timestamp = Date(),
                                category = selectedCategoryState.value
                            )
                            onSubmit(entity)
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Close")
                        Text(text = "保存", modifier = Modifier.padding(start = 8.dp))
                    }
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "新建文本收集",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                SheetLabel(text = "内容")
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = content.value,
                    onValueChange = { content.value = it },
                    minLines = 3,
                )
                SheetLabel(text = "标题")
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = title.value,
                    onValueChange = { title.value = it },
                )
                SheetLabel(text = "选择分类")
                LazyColumn {
                    CategorySelectForm(
                        categories = categories,
                        selected = selectedCategoryState.value,
                        onSelect = { selectedCategoryState.value = it },
                        onCreate = {},
                    )
                }
            }
        },
    )
}