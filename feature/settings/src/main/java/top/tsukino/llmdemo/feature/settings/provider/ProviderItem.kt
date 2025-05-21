package top.tsukino.llmdemo.feature.settings.provider

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.feature.settings.SettingsViewModel

@Composable
internal fun ProviderItem(
    vm: SettingsViewModel,
    provider: ProviderEntity
) {
    val showEditProviderDialog = remember { mutableStateOf<ProviderEntity?>(null) }
    val showManageProviderSheet = remember { mutableStateOf<ProviderEntity?>(null) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(
                Unit
            ) {
                detectTapGestures(
                    onLongPress = {
                        showManageProviderSheet.value = provider
                    },
                    onTap = {
                        showEditProviderDialog.value = provider
                    }
                )
            }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Text(provider.name, style = MaterialTheme.typography.labelLarge)
        Text(
            text = provider.host,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    when {
        showEditProviderDialog.value != null -> {
            ProviderDialog(
                target = showEditProviderDialog.value!!,
                title = { Text("修改模型提供商") },
                icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit a provider") },
                onConfirm = {
                    vm.addProvider(it)
                    showEditProviderDialog.value = null
                },
                onDismiss = {
                    showEditProviderDialog.value = null
                },
                onTestConnection = {
                    vm.getProviderModels(it)
                }
            )
        }
        showManageProviderSheet.value != null -> {
            ProviderManageSheet(
                provider = showManageProviderSheet.value!!,
                onDismiss = {
                    showManageProviderSheet.value = null
                },
                onDelete = { provider ->
                    vm.deleteProvider(provider)
                    showManageProviderSheet.value = null
                }
            )
        }
    }
}