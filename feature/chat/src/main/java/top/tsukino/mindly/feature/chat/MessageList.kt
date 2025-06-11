package top.tsukino.mindly.feature.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.dto.ConversationWithMessages
import top.tsukino.mindly.data.database.entity.MessageEntity
import top.tsukino.mindly.feature.common.MainController

@Composable
internal fun MessageList(
    mainController: MainController,
    conversation: ConversationWithMessages,
    state: LazyListState,
    onShowSheet: (Long) -> Unit,
) {
    LazyColumn(
        state = state,
        reverseLayout = true
    ) {
        items(
            conversation.messages.reversed(),
            key = { it.id }
        ) { item ->
            MessageContainer(
                modifier = Modifier.animateItem(),
                item = item,
                onShowSheet = onShowSheet
            )
        }
    }
}

@Composable
internal fun MessageContainer(
    item: MessageEntity,
    modifier: Modifier = Modifier,
    onShowSheet: (Long) -> Unit,
) {
    val bgColor = if (item.isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val alignment = if (item.isUser) {
        Alignment.CenterEnd
    } else {
        Alignment.CenterStart
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .clip(MaterialTheme.shapes.medium)
                .align(alignment)
                .widthIn(0.dp, 320.dp),
            color = bgColor
        ) {
            MessageItem(
                item = item,
                onShowSheet = onShowSheet,
            )
        }
    }
}

