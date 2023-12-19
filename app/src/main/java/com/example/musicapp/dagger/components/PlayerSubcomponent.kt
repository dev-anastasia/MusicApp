package com.example.musicapp.dagger.components

import com.example.musicapp.presentation.ui.player.PlayerFragment
import dagger.Subcomponent

@Subcomponent
interface PlayerSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PlayerSubcomponent
    }

    fun inject(fr: PlayerFragment)
}