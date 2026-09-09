package com.example.videoplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassProgressBar(
    progress: Float, // 0f to 1f
    bufferedProgress: Float = 0f,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    thumbGlow: Boolean = true
) {
    var widthPx by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .onSizeChanged { widthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val fraction = (offset.x / widthPx).coerceIn(0f, 1f)
                    onSeek(fraction)
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    val fraction = (change.position.x / widthPx).coerceIn(0f, 1f)
                    onSeek(fraction)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Track background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(Color(0x33FFFFFF))
        )

        // Buffer fill
        if (bufferedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(bufferedProgress.coerceIn(0f, 1f))
                    .height(height)
                    .clip(RoundedCornerShape(height / 2))
                    .background(Color(0x22FFFFFF))
            )
        }

        // Active progress fill (Liquid White with highlight)
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xDDFFFFFF),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
        )
    }
}

@Composable
fun GlassMiniSlider(
    value: Float, // 0f to 1f
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 90.dp,
    height: Dp = 8.dp
) {
    var widthPx by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = modifier
            .width(width)
            .height(20.dp)
            .onSizeChanged { widthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onValueChange((offset.x / widthPx).coerceIn(0f, 1f))
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    onValueChange((change.position.x / widthPx).coerceIn(0f, 1f))
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(Color(0x33FFFFFF))
        )

        // Active Fill
        Box(
            modifier = Modifier
                .fillMaxWidth(value.coerceIn(0f, 1f))
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xBBFFFFFF),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
        )
    }
}
