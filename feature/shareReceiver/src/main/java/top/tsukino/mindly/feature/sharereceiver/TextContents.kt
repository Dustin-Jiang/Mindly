package top.tsukino.mindly.feature.sharereceiver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownExtendedSpans
import top.tsukino.mindly.data.database.entity.CollectionTextEntity
import java.util.Date

class TextContents(
    val text: String,
    var title: String = "文本收集",
): CollectionItemDisplay<CollectionTextEntity> {
    @Composable
    override fun Display() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Markdown(
                        content = text,
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
            OutlinedTextField(
                value = title,
                onValueChange = { text ->
                    title = text
                },
                label = { Text("编辑标题") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    override fun toEntity(): CollectionTextEntity {
        val item = CollectionTextEntity(
            id = 0L,
            content = text,
            title = title,
            timestamp = Date()
        )
        return item
    }
}