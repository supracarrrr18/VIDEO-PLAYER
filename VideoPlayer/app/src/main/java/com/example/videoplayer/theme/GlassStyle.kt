package com.example.videoplayer.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class GlassStyle(
    val name: String,
    val backgroundBrush: Brush,
    val borderBrush: Brush,
    val borderWidth: Dp = 1.dp,
    val cornerRadius: Dp = 28.dp,
    val blurRadius: Dp = 24.dp,
    val contentColor: Color = Color.White,
    val accentColor: Color = Color(0xFF00E5FF),
    val shadowElevation: Dp = 8.dp
) {
    companion object {
        // Liquid Glass (Exact match to the uploaded visual reference)
        val LiquidGlass = GlassStyle(
            name = "Liquid Glass",
            backgroundBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x55444855),
                    Color(0x331C1D24)
                )
            ),
            borderBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x66FFFFFF),
                    Color(0x1AFFFFFF),
                    Color(0x0DFFFFFF)
                )
            ),
            borderWidth = 1.2.dp,
            cornerRadius = 32.dp,
            blurRadius = 28.dp,
            contentColor = Color(0xFFF5F6FA),
            accentColor = Color(0xFF00E5FF)
        )

        val DarkGlass = GlassStyle(
            name = "Dark Glass",
            backgroundBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x6615161E),
                    Color(0x880A0A0F)
                )
            ),
            borderBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x33FFFFFF),
                    Color(0x0AFFFFFF)
                )
            ),
            borderWidth = 1.dp,
            cornerRadius = 28.dp,
            blurRadius = 20.dp,
            contentColor = Color(0xFFEEEEEE),
            accentColor = Color(0xFF38BDF8)
        )

        val Amoled = GlassStyle(
            name = "AMOLED",
            backgroundBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xE6000000),
                    Color(0xFA000000)
                )
            ),
            borderBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x40FFFFFF),
                    Color(0x15FFFFFF)
                )
            ),
            borderWidth = 1.dp,
            cornerRadius = 24.dp,
            blurRadius = 0.dp,
            contentColor = Color.White,
            accentColor = Color(0xFF00E5FF)
        )
    }
}
