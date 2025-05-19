package top.tsukino.llmdemo.feature.settings

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import top.tsukino.llmdemo.data.database.entity.ProviderEntity

@Composable
fun ProviderDialog(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (ProviderEntity) -> Unit,
    onTestConnection: ((ProviderEntity) -> Unit)? = null,
    target: ProviderEntity = ProviderEntity(
        id = 0L,
        name = "",
        host = "",
        token = "",
        isEnabled = true
    )
) {
    val provider = remember { mutableStateOf<ProviderEntity>(value = target) }

    val isValid = remember(provider.value) {
        provider.value.name.isNotEmpty() && provider.value.host.isNotEmpty() && provider.value.token.isNotEmpty()
    }

    val showError = remember { mutableStateOf(false) }

    val beforeConfirm = {
        showError.value = false
        Log.d("AddProviderDialog", "onConfirm: ")
        if (isValid) {
            onConfirm(provider.value)
        } else {
            showError.value = true
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = icon,
        title = title,
        confirmButton = { TextButton(onClick = beforeConfirm) { Text("确认") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
        text = {
            Column {
                OutlinedTextField(
                    label = { Text("名称") },
                    value = provider.value.name,
                    isError = showError.value && provider.value.name.isEmpty(),
                    onValueChange = {
                        provider.value = provider.value.copy(name = it)
                    }
                )
                OutlinedTextField(
                    label = { Text("URL") },
                    value = provider.value.host,
                    isError = showError.value && provider.value.host.isEmpty(),
                    onValueChange = {
                        provider.value = provider.value.copy(host = it)
                    }
                )
                OutlinedTextField(
                    label = { Text("密钥") },
                    value = provider.value.token,
                    isError = showError.value && provider.value.token.isEmpty(),
                    onValueChange = {
                        provider.value = provider.value.copy(token = it)
                    },
                )
                if (onTestConnection != null) {
                    OutlinedButton(
                        onClick = { onTestConnection(target) },
                    ) {
                        Text("测试连接")
                    }
                }
            }
        }
    )
}