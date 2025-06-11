package top.tsukino.mindly.feature.settings.provider

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.feature.settings.SettingsViewModel

@Composable
internal fun AddProvider(
    vm: SettingsViewModel,
) {
    val showAddProviderDialog = remember { mutableStateOf(false) }

    Button(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { showAddProviderDialog.value = true },
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Provider"
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "添加模型提供商"
        )
    }

    when {
        showAddProviderDialog.value -> {
            ProviderDialog(
                title = { Text("添加模型提供商") },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a provider") },
                onConfirm = {
                    showAddProviderDialog.value = false
                    vm.addProvider(it)
                },
                onDismiss = {
                    showAddProviderDialog.value = false
                }
            )
        }
    }
}