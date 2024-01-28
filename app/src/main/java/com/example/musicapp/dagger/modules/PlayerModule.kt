package com.example.musicapp.dagger.modules

import com.example.musicapp.presentation.ui.player.PlayerClassImpl
import com.example.musicapp.presentation.ui.player.PlayerClass
import com.example.musicapp.presentation.ui.player.PlayerFragment
import dagger.Binds
import dagger.Module

@Module
interface PlayerModule {

    @Binds
    fun providesPlayerClass(impl: PlayerClassImpl): PlayerClass
}