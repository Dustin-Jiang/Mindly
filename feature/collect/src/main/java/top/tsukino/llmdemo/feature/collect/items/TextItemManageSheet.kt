package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.feature.common.component.SheetItem

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
internal fun TextItemManageSheet(
    id: Long,
    onDismiss: () -> Unit,
    onDelete: (Long) -> Unit,
    isSummaryTitleEnabled: Boolean,
    onSummary: (Long) -> Unit,
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
                text = "管理文本",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            SheetItem(
                text = { Text("生成总结标题") },
                icon = Icons.Filled.Title,
                disabled = !isSummaryTitleEnabled,
                onClick = { onSummary(id) }
            )
            SheetItem(
                text = { Text("删除") },
                icon = Icons.Default.Delete,
                warning = true,
                onClick = { onDelete(id) }
            )
        }
    }
}