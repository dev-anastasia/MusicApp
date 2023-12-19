package com.example.musicapp.dagger.modules

import com.example.musicapp.domain.player.PlayerClassImpl
import com.example.musicapp.presentation.PlayerClass
import dagger.Binds
import dagger.Module

@Module
interface PlayerModule {

    @Binds
    fun providesPlayerClass(playerClassImpl: PlayerClassImpl): PlayerClass
}