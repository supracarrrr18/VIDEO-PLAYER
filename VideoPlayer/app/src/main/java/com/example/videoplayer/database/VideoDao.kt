package com.example.videoplayer.database

import androidx.room.*
import com.example.videoplayer.data.VideoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY dateAdded DESC")
    fun getAllVideos(): Flow<List<VideoItem>>

    @Query("SELECT * FROM videos WHERE lastPlayedDate > 0 ORDER BY lastPlayedDate DESC LIMIT 10")
    fun getContinueWatching(): Flow<List<VideoItem>>

    @Query("SELECT * FROM videos WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavorites(): Flow<List<VideoItem>>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: String): VideoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoItem>)

    @Update
    suspend fun updateVideo(video: VideoItem)

    @Query("UPDATE videos SET lastPositionMs = :position, lastPlayedDate = :timestamp WHERE id = :id")
    suspend fun updateProgress(id: String, position: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE videos SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Delete
    suspend fun deleteVideo(video: VideoItem)

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteById(id: String)
}
