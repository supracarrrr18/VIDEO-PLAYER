package com.example.videoplayer.util

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.util.Rational
import android.view.WindowManager

object PipHelper {
    fun isPipSupported(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    fun enterPipMode(activity: Activity, aspectRatio: Rational = Rational(16, 9)) {
        if (!isPipSupported(activity)) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val validRatio = try {
                if (aspectRatio.toFloat() in 0.42f..2.38f) aspectRatio else Rational(16, 9)
            } catch (e: Exception) {
                Rational(16, 9)
            }

            val params = PictureInPictureParams.Builder()
                .setAspectRatio(validRatio)
                .build()
            activity.enterPictureInPictureMode(params)
        }
    }
}

class BrightnessVolumeHelper(private val activity: Activity) {
    private val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val maxVolume: Int
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    val currentVolume: Int
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun setVolume(volume: Int) {
        val safeVolume = volume.coerceIn(0, maxVolume)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, safeVolume, 0)
    }

    fun setVolumeFraction(fraction: Float) {
        setVolume((fraction * maxVolume).toInt())
    }

    fun getVolumeFraction(): Float {
        return currentVolume.toFloat() / maxVolume.coerceAtLeast(1).toFloat()
    }

    fun setScreenBrightness(brightness: Float) { // 0f to 1f, or -1f for system default
        val layout = activity.window.attributes
        layout.screenBrightness = brightness.coerceIn(0.01f, 1.0f)
        activity.window.attributes = layout
    }

    fun getScreenBrightness(): Float {
        val current = activity.window.attributes.screenBrightness
        return if (current < 0f) 0.5f else current
    }
}
