package com.example.videoplayer.subtitles

import androidx.media3.common.C
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubtitleManager(private val player: ExoPlayer) {

    private val _availableTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    val availableTracks: StateFlow<List<SubtitleTrack>> = _availableTracks.asStateFlow()

    private val _selectedTrack = MutableStateFlow<SubtitleTrack?>(null)
    val selectedTrack: StateFlow<SubtitleTrack?> = _selectedTrack.asStateFlow()

    var subtitleStyle = MutableStateFlow(SubtitleStyle())

    fun updateTracks(tracks: Tracks) {
        val list = mutableListOf<SubtitleTrack>()
        // Add "None" option
        list.add(SubtitleTrack(id = "none", name = "Off", isSelected = _selectedTrack.value == null))

        for (group in tracks.groups) {
            if (group.type == C.TRACK_TYPE_TEXT) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)
                    val isSelected = group.isTrackSelected(i)
                    val track = SubtitleTrack(
                        id = format.id ?: "$i",
                        name = format.label ?: format.language ?: "Track ${i + 1}",
                        language = format.language,
                        isSelected = isSelected
                    )
                    list.add(track)
                    if (isSelected) {
                        _selectedTrack.value = track
                    }
                }
            }
        }
        _availableTracks.value = list
    }

    fun selectTrack(track: SubtitleTrack) {
        _selectedTrack.value = if (track.id == "none") null else track
        val parameters = player.trackSelectionParameters.buildUpon()

        if (track.id == "none") {
            parameters.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
        } else {
            parameters.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
            if (track.language != null) {
                parameters.setPreferredTextLanguage(track.language)
            }
        }
        player.trackSelectionParameters = parameters.build()
    }
}
