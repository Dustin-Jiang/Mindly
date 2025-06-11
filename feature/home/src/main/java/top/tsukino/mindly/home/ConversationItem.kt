package top.tsukino.mindly.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.ConversationEntity
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.common.NavDest
import top.tsukino.mindly.feature.common.utils.DateTimeUtils

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
internal fun ConversationItem(
    mainController: MainController,
    item: ConversationEntity,
    onShowSheet: (Long) -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = {
                    mainController.navigate(NavDest.Chat(id = item.id))
                },
                onLongClick = {
                    onShowSheet(item.id)
                }
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = DateTimeUtils.formatReadableTime(item.timestamp) ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

