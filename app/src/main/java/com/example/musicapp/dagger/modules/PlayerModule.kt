package com.example.musicapp.dagger.modules

import com.example.musicapp.domain.player.PlayerClassImpl
import com.example.musicapp.presentation.PlayerClass
import com.example.musicapp.presentation.ui.player.PlayerFragment
import dagger.Binds
import dagger.Module

@Module
interface PlayerModule {

    fun inject(fr: PlayerFragment)

    @Binds
    fun providesPlayerClass(impl: PlayerClassImpl): PlayerClass
}