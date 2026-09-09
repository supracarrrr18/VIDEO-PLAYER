package com.example.videoplayer.database

import androidx.room.*
import com.example.videoplayer.data.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks WHERE videoId = :videoId ORDER BY positionMs ASC")
    fun getBookmarksForVideo(videoId: String): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE videoId = :videoId")
    suspend fun deleteBookmarksForVideo(videoId: String)
}
