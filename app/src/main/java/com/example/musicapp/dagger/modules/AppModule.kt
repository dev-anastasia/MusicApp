package com.example.musicapp.dagger.modules

import com.example.musicapp.application.MainApp
import dagger.Module

@Module
interface AppModule {

    fun inject(app: MainApp)
}