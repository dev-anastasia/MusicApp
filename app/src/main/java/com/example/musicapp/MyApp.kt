package com.example.musicapp

import android.app.Application
import android.content.Context
import com.example.musicapp.domain.database.PlaylistDatabase

class MyApp: Application() {

    fun initDatabase(context: Context) {
        Creator._dao = PlaylistDatabase.getDatabase(context).dao()
    }
}