package com.example.musicapp

import android.app.Application
import android.content.Context
import com.example.musicapp.dagger.DaggerAppComponent
import com.example.musicapp.domain.database.PlaylistDatabase

class MyApp: Application() {

    override fun onCreate() {

        val appComponent = DaggerAppComponent.create()
        super.onCreate()
    }

    fun initDatabase(context: Context) {
        Creator._dao = PlaylistDatabase.getDatabase(context).dao()
    }
}