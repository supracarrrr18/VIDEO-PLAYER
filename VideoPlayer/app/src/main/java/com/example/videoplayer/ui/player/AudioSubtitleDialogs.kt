package com.example.videoplayer.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoplayer.subtitles.SubtitleTrack
import com.example.videoplayer.ui.components.LiquidGlassSurface

@Composable
fun AudioEqualizerSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var eqEnabled by remember { mutableStateOf(true) }
    var bassBoost by remember { mutableFloatStateOf(0.4f) }
    var virtualizer by remember { mutableFloatStateOf(0.3f) }

    LiquidGlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Audio & 10-Band Equalizer",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = eqEnabled,
                    onCheckedChange = { eqEnabled = it }
                )
            }

            Text(
                text = "Spatial Virtualizer & Bass Engine",
                color = Color(0x99FFFFFF),
                fontSize = 12.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Bass Boost", color = Color.White)
                Text("${(bassBoost * 100).toInt()}%", color = Color(0xFF00E5FF))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("3D Virtualizer", color = Color.White)
                Text("${(virtualizer * 100).toInt()}%", color = Color(0xFF00E5FF))
            }
        }
    }
}

@Composable
fun SubtitleTrackSheet(
    tracks: List<SubtitleTrack>,
    selectedTrackId: String?,
    onSelectTrack: (SubtitleTrack) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Subtitles & Captions",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            tracks.forEach { track ->
                val isSelected = track.id == selectedTrackId || (selectedTrackId == null && track.id == "none")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isSelected) Color(0x3300E5FF) else Color(0x11FFFFFF),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectTrack(track) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.name,
                        color = if (isSelected) Color(0xFF00E5FF) else Color.White,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    if (isSelected) {
                        Text("Active", color = Color(0xFF00E5FF), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
