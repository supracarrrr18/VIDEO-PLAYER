package com.example.videoplayer.ui.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.videoplayer.ui.components.GlassCapsule
import com.example.videoplayer.ui.components.GlassCircleButton
import com.example.videoplayer.ui.components.GlassMiniSlider

@Composable
fun TopControlsBar(
    onCloseClick: () -> Unit,
    onPipClick: () -> Unit,
    onCastClick: () -> Unit,
    onShareClick: () -> Unit,
    volume: Float,
    isMuted: Boolean,
    onVolumeChange: (Float) -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TOP LEFT: [ Close ] [ PiP | Cast | Share ]
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // [ Close ] circular glass button
            GlassCircleButton(
                onClick = onCloseClick,
                size = 46.dp,
                icon = Icons.Default.Close,
                contentDescription = "Close"
            )

            // [ PiP | Cast | Share ] capsule pill
            GlassCapsule(
                height = 46.dp,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureInPictureAlt,
                        contentDescription = "PiP",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onPipClick
                            )
                    )
                    Icon(
                        imageVector = Icons.Default.Cast,
                        contentDescription = "Cast",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onCastClick
                            )
                    )
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onShareClick
                            )
                    )
                }
            }
        }

        // TOP RIGHT: [ Volume Slider | Speaker ]
        GlassCapsule(
            height = 46.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassMiniSlider(
                    value = volume,
                    onValueChange = onVolumeChange,
                    width = 84.dp
                )

                Icon(
                    imageVector = if (isMuted || volume == 0f) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = "Speaker",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onToggleMute
                        )
                )
            }
        }
    }
}
