package top.tsukino.llmdemo.feature.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.CustomMarkdownComponent
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownExtendedSpans
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import top.tsukino.llmdemo.data.database.dto.ConversationWithMessages
import top.tsukino.llmdemo.data.database.entity.MessageEntity
import top.tsukino.llmdemo.feature.common.MainController

@Composable
internal fun MessageList(
    mainController: MainController,
    conversation: ConversationWithMessages,
    state: LazyListState
) {
    LazyColumn(
        state = state
    ) {
        items(
            conversation.messages,
            key = { it.id }
        ) { item ->
            MessageItem(
                modifier = Modifier.animateItem(),
                item = item
            )
        }
    }
}

@Composable
internal fun MessageItem(
    item: MessageEntity,
    modifier: Modifier = Modifier,
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
            MessageItem(item)
        }
    }
}

