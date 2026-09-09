package com.example.videoplayer.renderer

enum class AspectRatioMode(val label: String, val ratio: Float?) {
    FIT("Fit to Screen", null),
    FILL("Fill (Crop)", null),
    ORIGINAL("100% Original", null),
    RATIO_16_9("16:9 Widescreen", 16f / 9f),
    RATIO_4_3("4:3 Standard", 4f / 3f),
    RATIO_21_9("21:9 Cinema", 21f / 9f),
    STRETCH("Stretch", null);

    fun next(): AspectRatioMode {
        val values = entries
        val nextIndex = (ordinal + 1) % values.size
        return values[nextIndex]
    }
}
