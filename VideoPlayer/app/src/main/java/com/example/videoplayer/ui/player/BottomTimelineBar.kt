package com.example.videoplayer.ui.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoplayer.ui.components.GlassCapsule
import com.example.videoplayer.ui.components.GlassProgressBar
import com.example.videoplayer.ui.components.LiquidGlassSurface

@Composable
fun BottomTimelineBar(
    title: String,
    subtitle: String,
    progress: Float,
    bufferedProgress: Float,
    onSeek: (Float) -> Unit,
    onInfoClick: () -> Unit,
    onInsightClick: () -> Unit,
    onContinueWatchingClick: () -> Unit,
    onSubtitlesClick: () -> Unit,
    onAudioClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        // Title and Subtitle
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = Color(0xCCFFFFFF),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
        }

        // Progress timeline (Sleek edge-to-edge glowing progress bar)
        GlassProgressBar(
            progress = progress,
            bufferedProgress = bufferedProgress,
            onSeek = onSeek,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Action controls below timeline
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left pills: [ Info ] [ InSight ] [ Continue Watching ]
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassActionPill(
                    label = "Info",
                    onClick = onInfoClick
                )

                GlassActionPill(
                    label = "InSight",
                    onClick = onInsightClick
                )

                GlassActionPill(
                    label = "Continue Watching",
                    onClick = onContinueWatchingClick
                )
            }

            // Right capsule: [ Subtitles | Audio | More ]
            GlassCapsule(
                height = 42.dp,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = "Subtitles",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onSubtitlesClick
                            )
                    )

                    Icon(
                        imageVector = Icons.Default.Equalizer,
                        contentDescription = "Audio & Equalizer",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onAudioClick
                            )
                    )

                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More & Filters",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onMoreClick
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun GlassActionPill(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        modifier = modifier
            .height(38.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
