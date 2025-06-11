package top.tsukino.mindly.feature.common.component.audioplayer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    @ApplicationContext private val application: Context
) : ViewModel(), Player.Listener {

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var currentFileUri: Uri? = null

    private var positionPollingJob: Job? = null

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(application).build().apply {
                addListener(this@AudioPlayerViewModel) // Listen to player events
                // Configure player settings if needed (e.g., setWakeMode)
            }
            // Start polling immediately, it will stop when not playing
            startPositionPolling()
        }
    }

    fun setAudioFile(filePath: String) {
        val file = File(application.getExternalFilesDir(null), filePath)
        if (!file.exists() || !file.isFile) {
            _playerState.update { it.copy(duration = 0L, current = 0L, isPlaying = false, seekEnabled = false) }
            release()
            throw IllegalArgumentException("Invalid audio file path: $filePath")
        }

        val uri = Uri.fromFile(file)
        if (uri != currentFileUri) {
            currentFileUri = uri
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare() // Prepare the player asynchronously
                _playerState.update { it.copy(isPlaying = false, duration = 0L, current = 0L, seekEnabled = false) }
            } ?: run {
                // If player was released due to invalid file, re-initialize and set file
                initializePlayer()
                setAudioFile(filePath) // Recursive call, but should only happen once
            }
        }
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
        _playerState.update { it.copy(current = position) }
    }

    fun seekBy(amount: Long) {
        exoPlayer?.apply {
            val newPosition = (currentPosition + amount).coerceIn(0L, duration.coerceAtLeast(0L))
            seekTo(newPosition)
        }
    }

    fun reset() {
        exoPlayer?.apply {
            pause()
            stop()
            clearMediaItems() // Clear current media
        }
        _playerState.value = AudioPlayerState()
        currentFileUri = null
        stopPositionPolling()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        _playerState.update { currentState ->
            when (playbackState) {
                Player.STATE_BUFFERING -> currentState.copy(isPlaying = exoPlayer?.playWhenReady == true) // Still playing if playWhenReady is true
                Player.STATE_READY -> currentState.copy(duration = exoPlayer?.duration ?: 0L, seekEnabled = exoPlayer?.isCurrentMediaItemSeekable ?: false)
                Player.STATE_ENDED -> currentState.copy(isPlaying = false, current = currentState.duration.coerceAtLeast(0L)) // Ensure current reaches total
                Player.STATE_IDLE -> currentState.copy(isPlaying = false, current = 0L, duration = 0L, seekEnabled = false) // No media or error
                else -> currentState
            }
        }
        // Stop polling when playback ends or becomes idle
        if (playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE) {
            stopPositionPolling()
        }
    }    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playerState.update { it.copy(
            isPlaying = isPlaying,
            current = exoPlayer?.currentPosition ?: 0L // Update position immediately when play state changes
        ) }
        // Start/stop polling based on playing state
        if (isPlaying) startPositionPolling() else stopPositionPolling()
    }

    override fun onEvents(player: Player, events: Player.Events) {
        // You can listen to other events here if needed, e.g., errors
        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) || events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
            // Handled in specific callbacks above
        }
        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION) || events.contains(Player.EVENT_MEDIA_METADATA_CHANGED)) {
            // Update metadata if you were showing title etc.
        }
        if (events.contains(Player.EVENT_POSITION_DISCONTINUITY) || events.contains(Player.EVENT_SEEK_BACK_INCREMENT_CHANGED) || events.contains(Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED)) {
            // Useful if you need to react to seeks precisely, though polling handles position updates
        }
    }
    // --- Position Polling ---
    private fun startPositionPolling() {
        stopPositionPolling() // Ensure any existing job is stopped
        positionPollingJob = viewModelScope.launch {
            while (isActive && (_playerState.value.isPlaying)) { // Poll while playing or buffering
                _playerState.update { currentState ->
                    currentState.copy(
                        current = exoPlayer?.currentPosition ?: 0L,
                        // You could also update buffered position here: bufferedPosition = exoPlayer?.bufferedPosition ?: 0L
                    )
                }
                delay(10) // Update UI every 10ms for smoother progress updates
            }
        }
    }

    private fun stopPositionPolling() {
        positionPollingJob?.cancel()
        positionPollingJob = null
    }


    // --- Lifecycle ---

    private fun release() {
        stopPositionPolling()
        exoPlayer?.apply {
            removeListener(this@AudioPlayerViewModel)
            release() // Release resources
        }
        exoPlayer = null
        currentFileUri = null
        _playerState.value = AudioPlayerState() // Reset state
    }

    override fun onCleared() {
        super.onCleared()
        release() // Release player when ViewModel is destroyed
    }
}