package com.example.videoplayer.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoplayer.ui.components.LiquidGlassSurface
import kotlinx.coroutines.delay

@Composable
fun GestureOverlay(
    onToggleControls: () -> Unit,
    onDoubleTapLeft: () -> Unit,
    onDoubleTapCenter: () -> Unit,
    onDoubleTapRight: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSeekPreview: (Long) -> Unit,
    onLongPressStart: () -> Unit,
    onLongPressEnd: () -> Unit,
    currentBrightness: Float,
    currentVolume: Float,
    modifier: Modifier = Modifier
) {
    var widthPx by remember { mutableFloatStateOf(1f) }
    var heightPx by remember { mutableFloatStateOf(1f) }

    var hudType by remember { mutableStateOf<String?>(null) } // "brightness", "volume", "seek", "speed2x"
    var hudValue by remember { mutableFloatStateOf(0f) }

    // Auto-hide HUD
    LaunchedEffect(hudType, hudValue) {
        if (hudType != null && hudType != "speed2x") {
            delay(1200)
            hudType = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                widthPx = it.width.toFloat()
                heightPx = it.height.toFloat()
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onToggleControls() },
                    onDoubleTap = { offset ->
                        val third = widthPx / 3f
                        when {
                            offset.x < third -> onDoubleTapLeft()
                            offset.x > third * 2 -> onDoubleTapRight()
                            else -> onDoubleTapCenter()
                        }
                    },
                    onLongPress = {
                        hudType = "speed2x"
                        onLongPressStart()
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (hudType == "speed2x") {
                            hudType = null
                            onLongPressEnd()
                        }
                    }
                ) { change, dragAmount ->
                    val posX = change.position.x
                    val isLeftSide = posX < widthPx / 2f
                    val deltaY = -dragAmount.y / heightPx

                    if (kotlin.math.abs(dragAmount.y) > kotlin.math.abs(dragAmount.x)) {
                        if (isLeftSide) {
                            // Left vertical swipe -> brightness
                            val newB = (currentBrightness + deltaY * 1.5f).coerceIn(0.01f, 1f)
                            hudType = "brightness"
                            hudValue = newB
                            onBrightnessChange(newB)
                        } else {
                            // Right vertical swipe -> volume
                            val newV = (currentVolume + deltaY * 1.5f).coerceIn(0f, 1f)
                            hudType = "volume"
                            hudValue = newV
                            onVolumeChange(newV)
                        }
                    } else if (kotlin.math.abs(dragAmount.x) > 10f) {
                        // Horizontal swipe -> Seek
                        val deltaMs = (dragAmount.x * 200).toLong()
                        hudType = "seek"
                        onSeekPreview(deltaMs)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Center Heads-Up Display (HUD)
        AnimatedVisibility(
            visible = hudType != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LiquidGlassSurface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (hudType) {
                        "brightness" -> {
                            Icon(Icons.Default.BrightnessMedium, contentDescription = null, tint = Color.White)
                            Text(
                                text = "Brightness ${(hudValue * 100).toInt()}%",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        "volume" -> {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
                            Text(
                                text = "Volume ${(hudValue * 100).toInt()}%",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        "speed2x" -> {
                            Icon(Icons.Default.FastForward, contentDescription = null, tint = Color(0xFF00E5FF))
                            Text(
                                text = "2.0x Fast Playback",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        "seek" -> {
                            Text(
                                text = "Seeking...",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
