package com.example.musicapp

import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.data.repos.SearchRepoImpl
import com.example.musicapp.data.repos.TrackInfoRepoImpl
import com.example.musicapp.domain.PlaylistsResultListener
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.useCases.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.GetTrackListUseCase
import com.example.musicapp.domain.useCases.PlaylistClickedUseCase
import com.example.musicapp.presentation.PlaylistsRepo
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.example.musicapp.presentation.presenters.SearchViewModel


object Creator {

    const val ARTIST_NAME = "artistName"
    const val TRACK_NAME = "trackName"
    const val DURATION = "duration"
    const val PREVIEW = "preview"
    const val COVER_IMAGE = "coverImage"

    private val searchRepo: SearchRepo = SearchRepoImpl() // Приводим к типу интерфейса
    val searchUseCase = GetTrackListUseCase(searchRepo)

    private val trackInfoRepo: TrackInfoRepo = TrackInfoRepoImpl()
    val trackInfoUseCase = GetTrackInfoUseCase(trackInfoRepo)

    private val playlistsRepo: PlaylistsRepo = PlaylistsRepoImpl()
    val playlistsUseCase = PlaylistClickedUseCase(playlistsRepo)

    fun setSearchUseCaseVM(vm: SearchViewModel) {
        searchUseCase.setVM(vm)
    }

    fun setPlayerUseCaseVM(vm: PlayerViewModel) {
        trackInfoUseCase.setVM(vm)
    }

    fun setPlaylistUseCaseVM(vm: PlaylistsResultListener) {
        playlistsUseCase.setVM(vm)
    }
}