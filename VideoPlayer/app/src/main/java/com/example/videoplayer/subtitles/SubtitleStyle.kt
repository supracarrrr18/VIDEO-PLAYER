package com.example.videoplayer.subtitles

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SubtitleTrack(
    val id: String,
    val name: String,
    val language: String? = null,
    val isExternal: Boolean = false,
    val uriString: String? = null,
    val isSelected: Boolean = false
)

data class SubtitleStyle(
    val fontSize: TextUnit = 20.sp,
    val textColor: Color = Color.White,
    val outlineColor: Color = Color.Black,
    val outlineWidth: Dp = 1.5.dp,
    val backgroundColor: Color = Color(0x66000000),
    val bottomOffset: Dp = 64.dp,
    val delayMs: Long = 0L
)
