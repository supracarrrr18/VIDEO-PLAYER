package com.example.videoplayer.playback

import com.example.videoplayer.data.VideoChapter
import com.example.videoplayer.filters.VideoFilter
import com.example.videoplayer.renderer.AspectRatioMode

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedPositionMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val isBuffering: Boolean = false,
    val isEnded: Boolean = false,
    val videoTitle: String = "The Studio",
    val videoSubtitle: String = "The Promotion",
    val volume: Float = 1.0f,
    val brightness: Float = 0.5f,
    val isMuted: Boolean = false,
    val isLocked: Boolean = false,
    val isPipActive: Boolean = false,
    val controlsVisible: Boolean = true,
    val aspectRatioMode: AspectRatioMode = AspectRatioMode.FIT,
    val currentFilter: VideoFilter = VideoFilter(),
    val isLoopingAB: Boolean = false,
    val loopPointA: Long? = null,
    val loopPointB: Long? = null,
    val chapters: List<VideoChapter> = emptyList(),
    val currentChapter: VideoChapter? = null
) {
    val progress: Float
        get() = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f

    val bufferedProgress: Float
        get() = if (durationMs > 0) (bufferedPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
}
