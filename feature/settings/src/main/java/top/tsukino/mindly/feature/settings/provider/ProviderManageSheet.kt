package top.tsukino.mindly.feature.settings.provider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.ProviderEntity
import top.tsukino.mindly.feature.common.component.SheetItem

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ProviderManageSheet(
    provider: ProviderEntity,
    onDismiss: () -> Unit,
    onDelete: (ProviderEntity) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "管理模型提供商",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            SheetItem(
                text = { Text("删除模型提供商") },
                icon = Icons.Default.Delete,
                warning = true,
                onClick = {
                    onDelete(provider)
                    onDismiss()
                }
            )
        }
    }
}