package com.example.musicapp.dagger.components

import com.example.musicapp.dagger.modules.AppSubcomponents
import com.example.musicapp.dagger.modules.PlayerModule
import com.example.musicapp.dagger.modules.PlaylistsModule
import com.example.musicapp.dagger.modules.TracksModule
import com.example.musicapp.presentation.ui.player.PlayerFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [PlaylistsModule::class, TracksModule::class, PlayerModule::class, AppSubcomponents::class]
)
interface AppComponent {

    fun mediaSubcomponent(): MediaSubcomponent.Factory

    fun searchSubcomponent(): SearchSubcomponent.Factory

    fun inject(fr: PlayerFragment)
}