package com.example.musicapp.application

import android.content.Context
import com.example.musicapp.dagger.AppComponent

val Context.component: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.component // рекурсивный вызов
    }