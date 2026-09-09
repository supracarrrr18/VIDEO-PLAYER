package com.example.videoplayer.data

data class FilterPreset(
    val id: String,
    val name: String,
    val brightness: Float = 0.0f,
    val contrast: Float = 1.0f,
    val saturation: Float = 1.0f,
    val exposure: Float = 0.0f,
    val gamma: Float = 1.0f,
    val temperature: Float = 0.0f,
    val tint: Float = 0.0f,
    val hue: Float = 0.0f,
    val vibrance: Float = 0.0f,
    val highlights: Float = 1.0f,
    val shadows: Float = 1.0f,
    val sharpness: Float = 0.0f,
    val vignette: Float = 0.0f,
    val filmGrain: Float = 0.0f
) {
    companion object {
        val ORIGINAL = FilterPreset(
            id = "original",
            name = "Original"
        )

        val CINEMA = FilterPreset(
            id = "cinema",
            name = "Cinema",
            contrast = 1.15f,
            saturation = 1.05f,
            temperature = -0.05f,
            shadows = 0.9f,
            highlights = 1.05f,
            vignette = 0.35f
        )

        val VIVID = FilterPreset(
            id = "vivid",
            name = "Vivid",
            contrast = 1.18f,
            saturation = 1.35f,
            vibrance = 0.4f,
            sharpness = 0.4f
        )

        val NATURAL = FilterPreset(
            id = "natural",
            name = "Natural",
            contrast = 1.02f,
            saturation = 0.98f,
            gamma = 1.02f
        )

        val NIGHT = FilterPreset(
            id = "night",
            name = "Night",
            brightness = -0.1f,
            contrast = 0.95f,
            temperature = 0.25f,
            tint = -0.1f,
            gamma = 0.95f
        )

        val FILM = FilterPreset(
            id = "film",
            name = "Film",
            contrast = 1.08f,
            saturation = 0.92f,
            shadows = 1.15f,
            highlights = 0.9f,
            filmGrain = 0.35f,
            vignette = 0.25f
        )

        val WARM = FilterPreset(
            id = "warm",
            name = "Warm",
            temperature = 0.35f,
            saturation = 1.08f
        )

        val COOL = FilterPreset(
            id = "cool",
            name = "Cool",
            temperature = -0.35f,
            contrast = 1.05f
        )

        val PRESETS = listOf(ORIGINAL, CINEMA, VIVID, NATURAL, NIGHT, FILM, WARM, COOL)
    }
}
