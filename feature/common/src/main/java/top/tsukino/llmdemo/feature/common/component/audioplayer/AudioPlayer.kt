package top.tsukino.llmdemo.feature.common.component.audioplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward5
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 1. 定义播放器状态数据类
data class AudioPlayerState(
    val current: Long = 0L, // in milliseconds
    val duration: Long = 0L, // in milliseconds
    val isPlaying: Boolean = false,
    val seekEnabled: Boolean = true // Whether seeking is possible
)

// Helper function to format milliseconds into MM:SS
fun Long.formatMilliseconds(): String {
    if (this < 0) return "00:00"
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

// 2. 创建音频播放器 Composable 函数
@Composable
fun AudioPlayer(
    state: AudioPlayerState,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSeek: (position: Long) -> Unit, // position in milliseconds
    onSeekForward: (amount: Long) -> Unit, // amount in milliseconds
    onSeekBackward: (amount: Long) -> Unit, // amount in milliseconds
    modifier: Modifier = Modifier,
    seekAmount: Long = 5000L // Default seek amount in milliseconds (e.g., 5 seconds)
) {
    // Local state to manage slider position while dragging
    var sliderPosition by remember(state.duration) {
        mutableFloatStateOf(
            if (state.duration > 0) {
                state.current.toFloat() / state.duration.toFloat()
            } else 0f
        )
    }
    var isSeeking by remember { mutableStateOf(false) }

    // Update slider position when not seeking (driven by external state updates)
    LaunchedEffect(state.current, state.duration, isSeeking) {
        if (!isSeeking && state.duration > 0) {
            sliderPosition = state.current.toFloat() / state.duration.toFloat()
        } else if (state.duration == 0L) {
            sliderPosition = 0f
        }
    }

    // Use Card for a Material You container with elevation and shape
    Card(
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium, // Use a standard Material shape
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Add elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Add inner padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between elements
        ) {
            // Optional: Track Info (can be added here)

            // Progress Slider
            Slider(
                value = sliderPosition,
                onValueChange = { newValue ->
                    isSeeking = true
                    sliderPosition = newValue
                },
                onValueChangeFinished = {
                    // Convert slider position (0f-1f) back to milliseconds
                    val newPosition = (sliderPosition * state.duration).toLong()
                        .coerceIn(0L, state.duration) // Ensure bounds
                    onSeek(newPosition)
                    isSeeking = false
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary, // Primary color for thumb
                    activeTrackColor = MaterialTheme.colorScheme.primary, // Primary color for active track
                    inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.24f) // Less prominent for inactive track
                ),
                enabled = state.seekEnabled && state.duration > 0 // Enable only if seekable and duration is known
            )

            // Time Display (Current / Total)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.current.formatMilliseconds(),
                    style = MaterialTheme.typography.labelSmall, // Smaller text style
                    color = MaterialTheme.colorScheme.onSurfaceVariant // A slightly subdued color
                )
                Text(
                    text = state.duration.formatMilliseconds(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly, // Evenly space buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind Button
                ControlIconButton(
                    icon = Icons.Filled.Replay5, // Or Replay10, Replay30
                    contentDescription = "Seek backward $seekAmount ms",
                    onClick = { onSeekBackward(seekAmount) },
                    enabled = state.seekEnabled && state.current > 0
                )

                // Play/Pause Button (larger)
                val playPauseIcon = if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow
                val playPauseContentDescription = if (state.isPlaying) "Pause" else "Play"
                val playPauseClick = if (state.isPlaying) onPauseClick else onPlayClick

                ControlIconButton(
                    icon = playPauseIcon,
                    contentDescription = playPauseContentDescription,
                    onClick = playPauseClick,
                    enabled = state.duration > 0 || state.isPlaying, // Enable if duration is known or already playing (e.g., streaming unknown duration)
                    iconSize = 48.dp // Make play/pause button larger
                )

                // Fast Forward Button
                ControlIconButton(
                    icon = Icons.Filled.Forward5, // Or Forward10, Forward30
                    contentDescription = "Seek forward $seekAmount ms",
                    onClick = { onSeekForward(seekAmount) },
                    enabled = state.seekEnabled && state.current < state.duration
                )
            }
        }
    }
}

// Helper Composable for control buttons to apply consistent styling
@Composable
fun ControlIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 36.dp // Default icon size
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(iconSize), // Use iconSize for the button size
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) // Adjust tint based on enabled state
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAudioPlayer_Playing() {
    MaterialTheme {
        AudioPlayer(
            state = AudioPlayerState(
                current = 30 * 1000L, // 30 seconds
                duration = 180 * 1000L, // 3 minutes
                isPlaying = true,
            ),
            onPlayClick = { /* Preview action */ },
            onPauseClick = { /* Preview action */ },
            onSeek = { /* Preview action */ },
            onSeekForward = { /* Preview action */ },
            onSeekBackward = { /* Preview action */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAudioPlayer_Paused() {
    MaterialTheme {
        AudioPlayer(
            state = AudioPlayerState(
                current = 60 * 1000L, // 1 minute
                duration = 240 * 1000L, // 4 minutes
                isPlaying = false,
            ),
            onPlayClick = { /* Preview action */ },
            onPauseClick = { /* Preview action */ },
            onSeek = { /* Preview action */ },
            onSeekForward = { /* Preview action */ },
            onSeekBackward = { /* Preview action */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAudioPlayer_Loading() {
    MaterialTheme {
        AudioPlayer(
            state = AudioPlayerState(
                current = 0L,
                duration = 0L, // Duration unknown or loading
                isPlaying = false,
                seekEnabled = false // Seek is disabled
            ),
            onPlayClick = { /* Preview action */ },
            onPauseClick = { /* Preview action */ },
            onSeek = { /* Preview action */ },
            onSeekForward = { /* Preview action */ },
            onSeekBackward = { /* Preview action */ }
        )
    }
}