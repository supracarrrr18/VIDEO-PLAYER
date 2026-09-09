package com.example.videoplayer.playback

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaybackController(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    val player: ExoPlayer = ExoPlayer.Builder(context)
        .setSeekBackIncrementMs(10000)
        .setSeekForwardIncrementMs(10000)
        .build()

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private var sleepTimerJob: Job? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) startProgressUpdates() else stopProgressUpdates()
            }

            override fun onPlaybackStateChanged(state: Int) {
                _playbackState.update {
                    it.copy(
                        isBuffering = state == Player.STATE_BUFFERING,
                        isEnded = state == Player.STATE_ENDED,
                        durationMs = player.duration.coerceAtLeast(0L)
                    )
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                updateProgress()
            }
        })
    }

    fun prepareMedia(uri: Uri, title: String, subtitle: String = "") {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        _playbackState.update {
            it.copy(
                videoTitle = title,
                videoSubtitle = subtitle,
                durationMs = player.duration.coerceAtLeast(0L)
            )
        }
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun play() = player.play()
    fun pause() = player.pause()

    fun seekBy(offsetMs: Long) {
        val target = (player.currentPosition + offsetMs).coerceIn(0L, player.duration.coerceAtLeast(0L))
        player.seekTo(target)
        updateProgress()
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs.coerceIn(0L, player.duration.coerceAtLeast(0L)))
        updateProgress()
    }

    fun seekToFraction(fraction: Float) {
        val target = (fraction * player.duration.coerceAtLeast(0L)).toLong()
        player.seekTo(target)
        updateProgress()
    }

    fun setPlaybackSpeed(speed: Float) {
        val safeSpeed = speed.coerceIn(0.25f, 4.0f)
        player.playbackParameters = PlaybackParameters(safeSpeed)
        _playbackState.update { it.copy(playbackSpeed = safeSpeed) }
    }

    fun setVolume(volume: Float) {
        val safeVol = volume.coerceIn(0f, 1f)
        player.volume = safeVol
        _playbackState.update { it.copy(volume = safeVol, isMuted = safeVol == 0f) }
    }

    fun toggleMute() {
        val currentMuted = _playbackState.value.isMuted
        if (currentMuted) {
            val restoreVol = 1.0f
            player.volume = restoreVol
            _playbackState.update { it.copy(volume = restoreVol, isMuted = false) }
        } else {
            player.volume = 0f
            _playbackState.update { it.copy(volume = 0f, isMuted = true) }
        }
    }

    fun setControlsVisibility(visible: Boolean) {
        _playbackState.update { it.copy(controlsVisible = visible) }
    }

    fun toggleControls() {
        _playbackState.update { it.copy(controlsVisible = !it.controlsVisible) }
    }

    fun setLoopAB(pointA: Long?, pointB: Long?) {
        _playbackState.update {
            it.copy(
                isLoopingAB = pointA != null && pointB != null,
                loopPointA = pointA,
                loopPointB = pointB
            )
        }
    }

    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) return
        sleepTimerJob = scope.launch {
            delay(minutes * 60 * 1000L)
            player.pause()
        }
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                updateProgress()
                checkABLoop()
                delay(200)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        updateProgress()
    }

    private fun updateProgress() {
        _playbackState.update {
            it.copy(
                currentPositionMs = player.currentPosition.coerceAtLeast(0L),
                durationMs = player.duration.coerceAtLeast(0L),
                bufferedPositionMs = player.bufferedPosition.coerceAtLeast(0L)
            )
        }
    }

    private fun checkABLoop() {
        val state = _playbackState.value
        if (state.isLoopingAB && state.loopPointA != null && state.loopPointB != null) {
            if (player.currentPosition >= state.loopPointB) {
                player.seekTo(state.loopPointA)
            }
        }
    }

    fun release() {
        stopProgressUpdates()
        sleepTimerJob?.cancel()
        player.release()
    }
}
