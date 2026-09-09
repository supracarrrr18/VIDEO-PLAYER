package com.example.videoplayer.ui.player

import android.app.Activity
import android.opengl.GLSurfaceView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.videoplayer.playback.PlayerViewModel
import com.example.videoplayer.renderer.GlVideoRenderer
import com.example.videoplayer.util.BrightnessVolumeHelper
import com.example.videoplayer.util.PipHelper

@Composable
fun LiquidGlassPlayerScreen(
    viewModel: PlayerViewModel,
    onClosePlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val hardwareHelper = remember(activity) { activity?.let { BrightnessVolumeHelper(it) } }

    val state by viewModel.playbackState.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    val selectedPreset by viewModel.selectedPreset.collectAsState()

    val showFilterSheet by viewModel.showFilterSheet.collectAsState()
    val showAudioEqSheet by viewModel.showAudioEqSheet.collectAsState()
    val showSubtitleSheet by viewModel.showSubtitleSheet.collectAsState()
    val showInfoSheet by viewModel.showInfoSheet.collectAsState()
    val showInsightSheet by viewModel.showInsightSheet.collectAsState()

    var glRenderer by remember { mutableStateOf<GlVideoRenderer?>(null) }

    // Keep GL renderer updated with current filter parameters in real-time
    LaunchedEffect(currentFilter) {
        glRenderer?.currentFilter = currentFilter
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Fullscreen GL Video Surface with real-time shader filter pipeline
        AndroidView(
            factory = { ctx ->
                GLSurfaceView(ctx).apply {
                    setEGLContextClientVersion(2)
                    val renderer = GlVideoRenderer(ctx, viewModel.controller.player)
                    renderer.currentFilter = currentFilter
                    setRenderer(renderer)
                    renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                    glRenderer = renderer
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Gesture Overlay (Brightness, Volume, Seek, 2x Long Press)
        GestureOverlay(
            onToggleControls = { viewModel.controller.toggleControls() },
            onDoubleTapLeft = { viewModel.controller.seekBy(-10000L) },
            onDoubleTapCenter = { viewModel.controller.togglePlayPause() },
            onDoubleTapRight = { viewModel.controller.seekBy(10000L) },
            onBrightnessChange = { b -> hardwareHelper?.setScreenBrightness(b) },
            onVolumeChange = { v ->
                viewModel.controller.setVolume(v)
                hardwareHelper?.setVolumeFraction(v)
            },
            onSeekPreview = { offset -> viewModel.controller.seekBy(offset) },
            onLongPressStart = { viewModel.controller.setPlaybackSpeed(2.0f) },
            onLongPressEnd = { viewModel.controller.setPlaybackSpeed(1.0f) },
            currentBrightness = hardwareHelper?.getScreenBrightness() ?: 0.5f,
            currentVolume = state.volume,
            modifier = Modifier.fillMaxSize()
        )

        // 3. Floating Liquid Glass Controls
        AnimatedVisibility(
            visible = state.controlsVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // TOP BAR
                TopControlsBar(
                    onCloseClick = onClosePlayer,
                    onPipClick = {
                        activity?.let { PipHelper.enterPipMode(it) }
                    },
                    onCastClick = { /* Cast remote screen */ },
                    onShareClick = { /* Share stream link */ },
                    volume = state.volume,
                    isMuted = state.isMuted,
                    onVolumeChange = { v ->
                        viewModel.controller.setVolume(v)
                        hardwareHelper?.setVolumeFraction(v)
                    },
                    onToggleMute = { viewModel.controller.toggleMute() },
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                // CENTER TRANSPORT CONTROLS
                CenterTransportControls(
                    isPlaying = state.isPlaying,
                    onPlayPauseClick = { viewModel.controller.togglePlayPause() },
                    onRewind10Click = { viewModel.controller.seekBy(-10000L) },
                    onForward10Click = { viewModel.controller.seekBy(10000L) },
                    modifier = Modifier.align(Alignment.Center)
                )

                // BOTTOM TIMELINE & ACTIONS
                BottomTimelineBar(
                    title = state.videoTitle,
                    subtitle = state.videoSubtitle,
                    progress = state.progress,
                    bufferedProgress = state.bufferedProgress,
                    onSeek = { fraction -> viewModel.controller.seekToFraction(fraction) },
                    onInfoClick = { viewModel.toggleInfoSheet() },
                    onInsightClick = { viewModel.toggleInsightSheet() },
                    onContinueWatchingClick = { /* Continue next episode */ },
                    onSubtitlesClick = { viewModel.toggleSubtitleSheet() },
                    onAudioClick = { viewModel.toggleAudioEqSheet() },
                    onMoreClick = { viewModel.toggleFilterSheet() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        // 4. Overlays & Slide-out Drawers
        if (showFilterSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000)),
                contentAlignment = Alignment.CenterEnd
            ) {
                FilterDrawer(
                    currentFilter = currentFilter,
                    selectedPreset = selectedPreset,
                    onPresetSelect = { viewModel.applyPreset(it) },
                    onFilterChange = { viewModel.updateFilter(it) },
                    onReset = { viewModel.resetFilter() },
                    onClose = { viewModel.toggleFilterSheet(false) }
                )
            }
        }

        if (showAudioEqSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000)),
                contentAlignment = Alignment.BottomCenter
            ) {
                AudioEqualizerSheet(
                    onClose = { viewModel.toggleAudioEqSheet(false) }
                )
            }
        }
    }
}
