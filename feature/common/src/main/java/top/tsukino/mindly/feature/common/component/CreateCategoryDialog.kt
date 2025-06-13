package top.tsukino.mindly.feature.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun CreateCategoryDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit,
) {
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
                    onDismiss()
                },
                enabled = title.value.isNotEmpty()
            ) { Text("确定") }
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