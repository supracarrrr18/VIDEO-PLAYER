package com.example.videoplayer

import android.app.Application
import com.example.videoplayer.database.AppDatabase

class VideoPlayerApplication : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getDatabase(this)
    }

    companion object {
        lateinit var instance: VideoPlayerApplication
            private set
    }
}
