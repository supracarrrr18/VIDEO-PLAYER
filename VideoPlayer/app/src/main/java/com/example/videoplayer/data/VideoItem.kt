package com.example.videoplayer.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoItem(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String = "",
    val uriString: String,
    val durationMs: Long = 0L,
    val lastPositionMs: Long = 0L,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastPlayedDate: Long = 0L,
    val sizeBytes: Long = 0L,
    val resolution: String = "",
    val mimeType: String = "video/*",
    val isFavorite: Boolean = false,
    val thumbnailUri: String? = null,
    val folderPath: String = ""
) {
    val uri: Uri
        get() = Uri.parse(uriString)

    val progressPercent: Float
        get() = if (durationMs > 0) (lastPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
}
