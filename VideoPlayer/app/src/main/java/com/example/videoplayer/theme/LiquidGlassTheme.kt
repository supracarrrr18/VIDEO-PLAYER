package com.example.videoplayer.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),
    secondary = Color(0xFF7C4DFF),
    background = Color(0xFF0A0A0F),
    surface = Color(0xFF14151F),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

val LocalGlassStyle = staticCompositionLocalOf { GlassStyle.LiquidGlass }

@Composable
fun VideoPlayerTheme(
    glassStyle: GlassStyle = GlassStyle.LiquidGlass,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalGlassStyle provides glassStyle) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            content = content
        )
    }
}
