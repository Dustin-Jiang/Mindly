package top.tsukino.mindly.feature.collect.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownExtendedSpans
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import top.tsukino.mindly.feature.common.utils.DateTimeUtils

class TextItem(
    val data: CollectionTextEntity,
    val title: String,
    override val category: Long? = data.category,

    val show: Boolean,

    val onShow: () -> Unit,
    val onShowSheet: () -> Unit,

    override val timestamp: Long = data.timestamp.time,
    override val id: ItemId
) : CollectItem {
    @OptIn(
        ExperimentalFoundationApi::class
    )
    @Composable
    override fun Display(
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onShow,
                    onLongClick = onShowSheet,
                ),
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = DateTimeUtils.formatReadableTime(data.timestamp) ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AnimatedVisibility(
                    visible = show,
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {},
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        SelectionContainer(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                        ) {
                            Markdown(
                                content = data.content,
                                colors = markdownColor(
                                    text = MaterialTheme.colorScheme.onSurface
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun CollectionTextEntity.toItemId(): ItemId {
    return ItemId("collection-text-${this.id}")
}