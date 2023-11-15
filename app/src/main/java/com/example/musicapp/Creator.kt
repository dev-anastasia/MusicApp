package com.example.musicapp

import com.example.musicapp.data.repos.GetTracksIdsRepoImpl
import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.data.repos.SearchRepoImpl
import com.example.musicapp.data.repos.TrackInfoRepoImpl
import com.example.musicapp.domain.GetTracksIdsRepo
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.useCases.GetTracksIdsListUseCase
import com.example.musicapp.domain.useCases.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.GetSearchTracksListUseCase
import com.example.musicapp.domain.useCases.PlaylistUseCase
import com.example.musicapp.presentation.PlaylistsRepo
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.example.musicapp.presentation.presenters.SearchViewModel


object Creator {

    const val ARTIST_NAME = "artistName"
    const val TRACK_NAME = "trackName"
    const val DURATION = "duration"
    const val PREVIEW = "preview"
    const val COVER_IMAGE = "coverImage"
    const val MEDIA_STATUS = "media status"
    const val LIKED_STATUS = "liked status"

    private val searchRepo: SearchRepo = SearchRepoImpl() // Приводим к типу интерфейса
    val searchUseCase = GetSearchTracksListUseCase(searchRepo)

    private val trackInfoRepo: TrackInfoRepo = TrackInfoRepoImpl()
    val trackInfoUseCase = GetTrackInfoUseCase(trackInfoRepo)

    private val playlistsRepo: PlaylistsRepo = PlaylistsRepoImpl()
    val playlistsUseCase = PlaylistUseCase(playlistsRepo)

    private val getTracksIdsRepo: GetTracksIdsRepo = GetTracksIdsRepoImpl()
    val favTracksRepoUseCase = GetTracksIdsListUseCase(getTracksIdsRepo)

    fun setSearchUseCaseVM(vm: SearchViewModel) {
        searchUseCase.setVM(vm)
    }

    fun setPlayerUseCaseVM(vm: PlayerViewModel) {
        trackInfoUseCase.setVM(vm)
    }
}