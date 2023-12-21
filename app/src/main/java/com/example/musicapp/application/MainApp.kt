package com.example.musicapp.application

import android.app.Application
import android.util.Log
import com.example.musicapp.dagger.components.AppComponent
import com.example.musicapp.dagger.components.DaggerAppComponent

class MainApp : Application() {

    lateinit var appComponent: AppComponent   // объект интерфейса AppComponent с inject-методами

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).build()
        Log.d("TAG", "MainApp onCreate()")
    }
}
