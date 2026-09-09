package com.example.videoplayer.audio

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.util.Log

class AudioEffectsManager(private val audioSessionId: Int) {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null

    var isEqEnabled: Boolean = false
        set(value) {
            field = value
            try { equalizer?.enabled = value } catch (e: Exception) { Log.w("AudioEffects", e.message ?: "") }
        }

    var isBassBoostEnabled: Boolean = false
        set(value) {
            field = value
            try { bassBoost?.enabled = value } catch (e: Exception) { Log.w("AudioEffects", e.message ?: "") }
        }

    var isVirtualizerEnabled: Boolean = false
        set(value) {
            field = value
            try { virtualizer?.enabled = value } catch (e: Exception) { Log.w("AudioEffects", e.message ?: "") }
        }

    var audioDelayMs: Long = 0L // Audio sync offset

    init {
        try {
            if (audioSessionId != 0) {
                equalizer = Equalizer(0, audioSessionId)
                bassBoost = BassBoost(0, audioSessionId)
                virtualizer = Virtualizer(0, audioSessionId)
            }
        } catch (e: Exception) {
            Log.e("AudioEffectsManager", "Failed to initialize hardware audio effects", e)
        }
    }

    fun getBandLevels(): ShortArray {
        val count = equalizer?.numberOfBands?.toInt() ?: 5
        val levels = ShortArray(count)
        val eq = equalizer ?: return levels
        for (i in 0 until count) {
            try {
                levels[i] = eq.getBandLevel(i.toShort())
            } catch (e: Exception) {
                levels[i] = 0
            }
        }
        return levels
    }

    fun setBandLevel(band: Short, level: Short) {
        try {
            equalizer?.setBandLevel(band, level)
        } catch (e: Exception) {
            Log.w("AudioEffectsManager", "Cannot set band level", e)
        }
    }

    fun setBassBoostStrength(strength: Short) { // 0 to 1000
        try {
            bassBoost?.setStrength(strength.coerceIn(0, 1000))
        } catch (e: Exception) {
            Log.w("AudioEffectsManager", "Cannot set bass boost", e)
        }
    }

    fun setVirtualizerStrength(strength: Short) { // 0 to 1000
        try {
            virtualizer?.setStrength(strength.coerceIn(0, 1000))
        } catch (e: Exception) {
            Log.w("AudioEffectsManager", "Cannot set virtualizer", e)
        }
    }

    fun release() {
        try {
            equalizer?.release()
            bassBoost?.release()
            virtualizer?.release()
        } catch (e: Exception) {
            Log.w("AudioEffectsManager", "Error releasing effects", e)
        }
        equalizer = null
        bassBoost = null
        virtualizer = null
    }
}
