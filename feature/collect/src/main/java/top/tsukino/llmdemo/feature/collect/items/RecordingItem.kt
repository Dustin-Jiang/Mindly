package top.tsukino.llmdemo.feature.collect.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import top.tsukino.llmdemo.data.database.entity.RecordingEntity
import top.tsukino.llmdemo.feature.collect.CollectViewModel
import top.tsukino.llmdemo.feature.common.component.audioplayer.AudioPlayer
import top.tsukino.llmdemo.feature.common.component.audioplayer.AudioPlayerState
import top.tsukino.llmdemo.feature.common.component.audioplayer.AudioPlayerViewModel
import top.tsukino.llmdemo.feature.common.utils.DateTimeUtils
import java.util.Date

class RecordingItem(
    val data: RecordingEntity,
    override val timestamp: Long = data.timestamp.time,

    private val show: Boolean,

    private val playerState: AudioPlayerState,

    private val onShow: (path: String) -> Unit,

    private val onPlayClick: () -> Unit,
    private val onPauseClick: () -> Unit,
    private val onSeek: (Long) -> Unit,
    private val onSeekForward: (Long) -> Unit,
    private val onSeekBackward: (Long) -> Unit,
) : CollectItem {
    @Composable
    override fun Display() {
        Column(
            modifier = Modifier.fillMaxWidth().clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = true,
                ),
                onClick = {
                    onShow(data.path)
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
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

            AnimatedVisibility(show) {
                AudioPlayer(
                    state = playerState,
                    onPlayClick = onPlayClick,
                    onPauseClick = onPauseClick,
                    onSeek = onSeek,
                    onSeekForward = onSeekForward,
                    onSeekBackward = onSeekBackward,
                )
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