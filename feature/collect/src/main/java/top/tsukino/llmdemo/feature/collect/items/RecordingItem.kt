package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.data.database.entity.RecordingEntity
import top.tsukino.llmdemo.feature.common.component.audioplayer.AudioPlayer
import top.tsukino.llmdemo.feature.common.component.audioplayer.AudioPlayerState
import top.tsukino.llmdemo.feature.common.utils.DateTimeUtils

class RecordingItem(
    val data: RecordingEntity,
    override val id: ItemId,
    override val timestamp: Long = data.timestamp.time,
    override val category: Long? = data.category,

    private val show: Boolean,

    private val playerState: AudioPlayerState,

    private val onShow: () -> Unit,
    private val onShowManageSheet: (Long) -> Unit,

    private val onPlayClick: () -> Unit,
    private val onPauseClick: () -> Unit,
    private val onSeek: (Long) -> Unit,
    private val onSeekForward: (Long) -> Unit,
    private val onSeekBackward: (Long) -> Unit,
) : CollectItem {
    @OptIn(
        ExperimentalFoundationApi::class
    )
    @Composable
    override fun Display(
        modifier: Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onShow()
                    },
                    onLongClick = {
                        onShowManageSheet(data.id)
                    }
                )
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = data.duration.toDuration(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = DateTimeUtils.formatReadableTime(data.timestamp) ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AnimatedVisibility(
                    visible = show,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AudioPlayer(
                            state = playerState,
                            onPlayClick = onPlayClick,
                            onPauseClick = onPauseClick,
                            onSeek = onSeek,
                            onSeekForward = onSeekForward,
                            onSeekBackward = onSeekBackward,
                        )
                        AnimatedVisibility(data.transcript.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {},
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Add elevation
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 16.dp),
                                ) {
                                    Text(
                                        text = data.transcript,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    internal fun Long.toDuration(): String {
        var seconds = this / 1000
        var minutes = seconds / 60
        val hours = minutes / 60
        seconds = seconds % 60
        minutes = minutes % 60
        return buildString {
            if (hours > 0) {
                append(String.format("%02d:", hours))
            }
            append(String.format("%02d:", minutes))
            append(String.format("%02d", seconds))
        }
    }
}

fun RecordingEntity.toItemId(): ItemId {
    return ItemId("recording-item-${this.id}")
}