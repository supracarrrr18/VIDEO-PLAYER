package com.example.videoplayer.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoplayer.data.FilterPreset
import com.example.videoplayer.filters.VideoFilter
import com.example.videoplayer.ui.components.LiquidGlassSurface

@Composable
fun FilterDrawer(
    currentFilter: VideoFilter,
    selectedPreset: FilterPreset,
    onPresetSelect: (FilterPreset) -> Unit,
    onFilterChange: (VideoFilter) -> Unit,
    onReset: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        modifier = modifier
            .fillMaxHeight()
            .width(360.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GPU Color Grading",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Reset",
                    color = Color(0xFF00E5FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onReset() }
                )
            }

            // Presets row
            Text(
                text = "PRESETS",
                color = Color(0x99FFFFFF),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(FilterPreset.PRESETS) { preset ->
                    val isSelected = preset.id == selectedPreset.id
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) Color(0xFF00E5FF) else Color(0x33FFFFFF),
                                CircleShape
                            )
                            .clickable { onPresetSelect(preset) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = preset.name,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Fine-tuning Sliders
            Text(
                text = "ADJUSTMENTS",
                color = Color(0x99FFFFFF),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            FilterSliderItem("Brightness", currentFilter.brightness, -1f, 1f) {
                onFilterChange(currentFilter.copy(brightness = it))
            }
            FilterSliderItem("Contrast", currentFilter.contrast, 0.5f, 2f) {
                onFilterChange(currentFilter.copy(contrast = it))
            }
            FilterSliderItem("Saturation", currentFilter.saturation, 0f, 2f) {
                onFilterChange(currentFilter.copy(saturation = it))
            }
            FilterSliderItem("Exposure", currentFilter.exposure, -2f, 2f) {
                onFilterChange(currentFilter.copy(exposure = it))
            }
            FilterSliderItem("Gamma", currentFilter.gamma, 0.5f, 2f) {
                onFilterChange(currentFilter.copy(gamma = it))
            }
            FilterSliderItem("Temperature", currentFilter.temperature, -1f, 1f) {
                onFilterChange(currentFilter.copy(temperature = it))
            }
            FilterSliderItem("Tint", currentFilter.tint, -1f, 1f) {
                onFilterChange(currentFilter.copy(tint = it))
            }
            FilterSliderItem("Vibrance", currentFilter.vibrance, -1f, 1f) {
                onFilterChange(currentFilter.copy(vibrance = it))
            }
            FilterSliderItem("Sharpness", currentFilter.sharpness, 0f, 2f) {
                onFilterChange(currentFilter.copy(sharpness = it))
            }
            FilterSliderItem("Vignette", currentFilter.vignette, 0f, 1f) {
                onFilterChange(currentFilter.copy(vignette = it))
            }
            FilterSliderItem("Film Grain", currentFilter.filmGrain, 0f, 1f) {
                onFilterChange(currentFilter.copy(filmGrain = it))
            }
        }
    }
}

@Composable
private fun FilterSliderItem(
    label: String,
    value: Float,
    rangeMin: Float,
    rangeMax: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White, fontSize = 13.sp)
            Text(String.format("%.2f", value), color = Color(0xAAFFFFFF), fontSize = 12.sp)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = rangeMin..rangeMax,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color(0xFF00E5FF),
                inactiveTrackColor = Color(0x33FFFFFF)
            )
        )
    }
}
