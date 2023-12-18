package com.example.musicapp.dagger

import com.example.musicapp.domain.useCases.playlists.DeletePlaylistUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistInfoUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.playlists.InsertPlaylistUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.ui.search.SearchFragment
import dagger.Component

@Component(modules = [PlaylistsModule::class, TracksModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(fr: SearchFragment)

    fun getTracksListUseCase(): GetTracksListUseCase

    fun getPlaylistsUseCase(): GetPlaylistsUseCase

    fun insertPlaylistUseCase(): InsertPlaylistUseCase

    fun deletePlaylistUseCase(): DeletePlaylistUseCase

    fun getPlaylistInfoUseCase(): GetPlaylistInfoUseCase
}