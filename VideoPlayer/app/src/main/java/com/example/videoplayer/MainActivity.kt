package com.example.videoplayer

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.videoplayer.data.VideoItem
import com.example.videoplayer.playback.PlayerViewModel
import com.example.videoplayer.theme.VideoPlayerTheme
import com.example.videoplayer.ui.home.HomeScreen
import com.example.videoplayer.ui.player.LiquidGlassPlayerScreen
import com.example.videoplayer.util.PipHelper

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()

        handleIntent(intent)

        setContent {
            VideoPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0A0A0F)
                ) {
                    var isPlayingVideo by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        // Sample demonstration video matching user's visual reference
                        val sampleVideo = VideoItem(
                            id = "sample_studio",
                            title = "The Studio",
                            subtitle = "The Promotion",
                            uriString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                            durationMs = 596000L
                        )
                        playerViewModel.loadVideo(sampleVideo)
                        isPlayingVideo = true
                    }

                    if (isPlayingVideo) {
                        LiquidGlassPlayerScreen(
                            viewModel = playerViewModel,
                            onClosePlayer = {
                                playerViewModel.controller.pause()
                                isPlayingVideo = false
                            }
                        )
                    } else {
                        HomeScreen(
                            continueWatchingVideos = listOf(
                                VideoItem(
                                    id = "sample_studio",
                                    title = "The Studio",
                                    subtitle = "The Promotion",
                                    uriString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                    durationMs = 596000L,
                                    lastPositionMs = 124000L
                                )
                            ),
                            allVideos = listOf(
                                VideoItem(
                                    id = "sample_studio",
                                    title = "The Studio",
                                    subtitle = "The Promotion",
                                    uriString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                    durationMs = 596000L
                                )
                            ),
                            onVideoClick = { video ->
                                playerViewModel.loadVideo(video)
                                isPlayingVideo = true
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data ?: return
        val title = intent.getStringExtra("title") ?: uri.lastPathSegment ?: "Video Playback"
        playerViewModel.loadUri(uri, title)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (playerViewModel.playbackState.value.isPlaying) {
            PipHelper.enterPipMode(this)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        playerViewModel.controller.setControlsVisibility(!isInPictureInPictureMode)
    }

    private fun hideSystemBars() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }
}
