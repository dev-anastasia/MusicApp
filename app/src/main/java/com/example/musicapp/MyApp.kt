package com.example.musicapp

import android.app.Application
import com.example.musicapp.domain.database.PlaylistDatabase

class MyApp: Application() {

    fun initDatabase() {
        Creator.dao = PlaylistDatabase.getDatabase(this.applicationContext).dao()
    }
}