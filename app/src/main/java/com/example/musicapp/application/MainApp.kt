package com.example.musicapp.application

import android.app.Application
import android.content.Context
import com.example.musicapp.Creator
import com.example.musicapp.dagger.components.AppComponent
import com.example.musicapp.dagger.components.DaggerAppComponent
import com.example.musicapp.domain.database.PlaylistDatabase

class MainApp : Application() {

    lateinit var appComponent: AppComponent // объект интерфейса AppComponent с inject-методами
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

    fun initDatabase(context: Context) {
        Creator.dao = PlaylistDatabase.getDatabase(context).dao()
    }
}
