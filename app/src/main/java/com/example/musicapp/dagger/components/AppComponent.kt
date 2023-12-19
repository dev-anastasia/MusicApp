package com.example.musicapp.dagger.components

import com.example.musicapp.dagger.modules.PlayerModule
import com.example.musicapp.dagger.modules.PlaylistsModule
import com.example.musicapp.dagger.modules.ReposImplModule
import com.example.musicapp.dagger.modules.TracksModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        PlaylistsModule::class,
        TracksModule::class,
        PlayerModule::class,
        AppSubcomponents::class,
        ReposImplModule::class
    ]
)
interface AppComponent {

    fun mediaSubcomponent(): MediaSubcomponent.Factory

    fun searchSubcomponent(): SearchSubcomponent.Factory
}