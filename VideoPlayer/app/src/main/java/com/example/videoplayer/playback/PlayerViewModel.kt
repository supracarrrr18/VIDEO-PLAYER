package com.example.videoplayer.playback

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayer.VideoPlayerApplication
import com.example.videoplayer.data.FilterPreset
import com.example.videoplayer.data.VideoItem
import com.example.videoplayer.filters.VideoFilter
import com.example.videoplayer.renderer.AspectRatioMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    val controller = PlaybackController(application)
    val playbackState = controller.playbackState

    private val db = (application as VideoPlayerApplication).database

    private val _currentFilter = MutableStateFlow(VideoFilter())
    val currentFilter: StateFlow<VideoFilter> = _currentFilter.asStateFlow()

    private val _selectedPreset = MutableStateFlow(FilterPreset.ORIGINAL)
    val selectedPreset: StateFlow<FilterPreset> = _selectedPreset.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    private val _showAudioEqSheet = MutableStateFlow(false)
    val showAudioEqSheet: StateFlow<Boolean> = _showAudioEqSheet.asStateFlow()

    private val _showSubtitleSheet = MutableStateFlow(false)
    val showSubtitleSheet: StateFlow<Boolean> = _showSubtitleSheet.asStateFlow()

    private val _showInfoSheet = MutableStateFlow(false)
    val showInfoSheet: StateFlow<Boolean> = _showInfoSheet.asStateFlow()

    private val _showInsightSheet = MutableStateFlow(false)
    val showInsightSheet: StateFlow<Boolean> = _showInsightSheet.asStateFlow()

    private var currentVideoId: String = ""

    fun loadVideo(videoItem: VideoItem) {
        currentVideoId = videoItem.id
        controller.prepareMedia(videoItem.uri, videoItem.title, videoItem.subtitle)
        if (videoItem.lastPositionMs > 0) {
            controller.seekTo(videoItem.lastPositionMs)
        }
        controller.play()
    }

    fun loadUri(uri: Uri, title: String = "The Studio", subtitle: String = "The Promotion") {
        currentVideoId = uri.toString()
        controller.prepareMedia(uri, title, subtitle)
        controller.play()
    }

    fun applyPreset(preset: FilterPreset) {
        _selectedPreset.value = preset
        val filter = VideoFilter(
            brightness = preset.brightness,
            contrast = preset.contrast,
            saturation = preset.saturation,
            exposure = preset.exposure,
            gamma = preset.gamma,
            temperature = preset.temperature,
            tint = preset.tint,
            hue = preset.hue,
            vibrance = preset.vibrance,
            highlights = preset.highlights,
            shadows = preset.shadows,
            sharpness = preset.sharpness,
            vignette = preset.vignette,
            filmGrain = preset.filmGrain
        )
        _currentFilter.value = filter
    }

    fun updateFilter(filter: VideoFilter) {
        _currentFilter.value = filter
    }

    fun resetFilter() {
        applyPreset(FilterPreset.ORIGINAL)
    }

    fun toggleFilterSheet(show: Boolean? = null) {
        _showFilterSheet.update { show ?: !it }
    }

    fun toggleAudioEqSheet(show: Boolean? = null) {
        _showAudioEqSheet.update { show ?: !it }
    }

    fun toggleSubtitleSheet(show: Boolean? = null) {
        _showSubtitleSheet.update { show ?: !it }
    }

    fun toggleInfoSheet(show: Boolean? = null) {
        _showInfoSheet.update { show ?: !it }
    }

    fun toggleInsightSheet(show: Boolean? = null) {
        _showInsightSheet.update { show ?: !it }
    }

    fun cycleAspectRatio() {
        val nextMode = playbackState.value.aspectRatioMode.next()
        // update controller or state
    }

    fun saveProgress() {
        if (currentVideoId.isNotEmpty()) {
            val pos = playbackState.value.currentPositionMs
            viewModelScope.launch {
                db.videoDao().updateProgress(currentVideoId, pos)
            }
        }
    }

    override fun onCleared() {
        saveProgress()
        controller.release()
        super.onCleared()
    }
}
