package top.tsukino.mindly.feature.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import top.tsukino.mindly.feature.common.component.SheetItem
import top.tsukino.mindly.feature.common.component.SheetLabel

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun MessageSheet(
    id: Long,
    onDismiss: () -> Unit,
    onDelete: (Long) -> Unit = {},
    onShare: (Long) -> Unit = {},
    onArchive: (Long) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Column {
            SheetLabel("消息操作")

            SheetItem(
                text = { Text("添加到收藏") },
                icon = Icons.Default.Archive,
                onClick = {
                    onArchive(id)
                    onDismiss()
                }
            )

            SheetItem(
                text = { Text("分享") },
                icon = Icons.Default.Share,
                onClick = {
                    onShare(id)
                    onDismiss()
                }
            )

            SheetItem(
                text = { Text("删除") },
                icon = Icons.Default.Delete,
                warning = true,
                onClick = {
                    onDelete(id)
                    onDismiss()
                }
            )
        }
    }
}