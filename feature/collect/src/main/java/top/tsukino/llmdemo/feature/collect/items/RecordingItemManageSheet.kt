package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.feature.common.component.SheetIconItem
import top.tsukino.llmdemo.feature.common.component.SheetItem
import top.tsukino.llmdemo.feature.common.component.SheetLabel

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
internal fun RecordingItemManageSheet(
    id: Long,
    onDismiss: () -> Unit,
    onDelete: (Long) -> Unit,
    isTranscriptEnabled: Boolean,
    onTranscript: (Long) -> Unit,
    isSummaryTitleEnabled: Boolean,
    onSummary: (Long) -> Unit,

    onCreateConversation: (Long) -> Unit,
    onShareTranscript: (Long) -> Unit,
    onSelectCategory: (Long) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SheetLabel("操作")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SheetIconItem(
                    text = "创建新对话",
                    icon = Icons.Filled.AddComment,
                    onClick = { onCreateConversation(id) }
                )
                SheetIconItem(
                    text = "分享文本",
                    icon = Icons.Filled.Share,
                    onClick = { onShareTranscript(id) }
                )
            }
            SheetLabel("管理语音")
            SheetItem(
                text = { Text("转录为文本") },
                icon = Icons.Filled.RecordVoiceOver,
                disabled = !isTranscriptEnabled,
                onClick = { onTranscript(id) }
            )
            SheetItem(
                text = { Text("生成总结标题") },
                icon = Icons.Filled.Title,
                disabled = !isSummaryTitleEnabled,
                onClick = { onSummary(id) }
            )
            SheetItem(
                text = { Text("选择分类") },
                icon = Icons.AutoMirrored.Filled.DriveFileMove,
                onClick = { onSelectCategory(id) }
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