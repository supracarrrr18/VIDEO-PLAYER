package com.example.videoplayer.filters

data class VideoFilter(
    val brightness: Float = 0.0f,     // -1.0 to 1.0
    val contrast: Float = 1.0f,       // 0.0 to 2.0
    val saturation: Float = 1.0f,     // 0.0 to 2.0
    val exposure: Float = 0.0f,       // -2.0 to 2.0
    val gamma: Float = 1.0f,          // 0.2 to 3.0
    val temperature: Float = 0.0f,    // -1.0 to 1.0
    val tint: Float = 0.0f,           // -1.0 to 1.0
    val hue: Float = 0.0f,            // 0.0 to 360.0
    val vibrance: Float = 0.0f,       // -1.0 to 1.0
    val highlights: Float = 1.0f,     // 0.0 to 2.0
    val shadows: Float = 1.0f,        // 0.0 to 2.0
    val sharpness: Float = 0.0f,      // 0.0 to 2.0
    val vignette: Float = 0.0f,       // 0.0 to 1.0
    val filmGrain: Float = 0.0f       // 0.0 to 1.0
) {
    val isDefault: Boolean
        get() = brightness == 0f && contrast == 1f && saturation == 1f &&
                exposure == 0f && gamma == 1f && temperature == 0f &&
                tint == 0f && hue == 0f && vibrance == 0f &&
                highlights == 1f && shadows == 1f && sharpness == 0f &&
                vignette == 0f && filmGrain == 0f
}
