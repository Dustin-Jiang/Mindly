package top.tsukino.mindly.feature.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownExtendedSpans
import top.tsukino.mindly.data.database.entity.MessageEntity

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    item: MessageEntity,
    onShowSheet: (Long) -> Unit,
) {
    val textColor = if (item.isUser) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    Column(
        modifier = modifier.padding(
            vertical = 8.dp,
            horizontal = 16.dp
        ),
    ) {
        SelectionContainer {
            Markdown(
                content = item.text,
                colors = markdownColor(
                    text = textColor
                ),
                typography = markdownTypography(
                    h1 = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    h2 = MaterialTheme.typography.headlineSmall,
                    h3 = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    h4 = MaterialTheme.typography.titleLarge,
                    h5 = MaterialTheme.typography.titleMedium,
                    h6 = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                ),
                extendedSpans = markdownExtendedSpans {
                    remember {
                        ExtendedSpans(
                            RoundedCornerSpanPainter(),
                        )
                    }
                },
                animations = markdownAnimations { this },
            )
        }

        if (item.isUser.not()) {
            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = 4.dp
                ),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item.endReason?.let {
                    Card(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        colors = CardDefaults
                            .cardColors()
                            .copy(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Box(
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            ),
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
                IconButton(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterEnd),
                    onClick = {
                        onShowSheet(item.id)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}