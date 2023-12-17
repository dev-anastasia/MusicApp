package com.example.musicapp.dagger

import com.example.musicapp.domain.PlaylistsRepo
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun playlistsRepo(): PlaylistsRepo
}