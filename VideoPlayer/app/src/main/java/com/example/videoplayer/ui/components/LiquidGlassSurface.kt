package com.example.videoplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.videoplayer.theme.GlassStyle
import com.example.videoplayer.theme.LocalGlassStyle

@Composable
fun LiquidGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    glassStyle: GlassStyle = LocalGlassStyle.current,
    elevation: Dp = glassStyle.shadowElevation,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false
            )
            .clip(shape)
            .background(glassStyle.backgroundBrush)
            .border(
                width = glassStyle.borderWidth,
                brush = glassStyle.borderBrush,
                shape = shape
            ),
        content = content
    )
}
