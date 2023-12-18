package com.example.musicapp.application

import android.app.Application
import android.content.Context
import com.example.musicapp.Creator
import com.example.musicapp.dagger.AppComponent
import com.example.musicapp.dagger.DaggerAppComponent
import com.example.musicapp.domain.database.PlaylistDatabase

class MainApp : Application() {

    lateinit var appComponent: AppComponent // объект интерфейса с inject-методами
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

    fun initDatabase(context: Context) {
        Creator._dao = PlaylistDatabase.getDatabase(context).dao()
    }
}
