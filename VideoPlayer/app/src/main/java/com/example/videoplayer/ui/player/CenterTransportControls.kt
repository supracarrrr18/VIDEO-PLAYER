package com.example.videoplayer.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoplayer.ui.components.GlassCircleButton

@Composable
fun CenterTransportControls(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onRewind10Click: () -> Unit,
    onForward10Click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [ 10 sec rewind ]
        GlassCircleButton(
            onClick = onRewind10Click,
            size = 56.dp,
            contentDescription = "Rewind 10 seconds"
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // [ PLAY / PAUSE ] Prominent center glass circle (76dp)
        GlassCircleButton(
            onClick = onPlayPauseClick,
            size = 76.dp,
            icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            iconSize = 38.dp,
            contentDescription = if (isPlaying) "Pause" else "Play"
        )

        // [ 10 sec forward ]
        GlassCircleButton(
            onClick = onForward10Click,
            size = 56.dp,
            contentDescription = "Forward 10 seconds"
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
