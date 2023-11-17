package com.example.musicapp

import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.data.repos.TrackInfoRepoImpl
import com.example.musicapp.data.repos.TracksRepoImpl
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.useCases.playlists.DeletePlaylistUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.playlists.InsertPlaylistUseCase
import com.example.musicapp.domain.useCases.tracks.DeleteTrackUseCase
import com.example.musicapp.domain.useCases.tracks.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.domain.useCases.tracks.InsertTrackUseCase
import com.example.musicapp.presentation.PlaylistsRepo

object Creator {

    private val trackInfoRepo: TrackInfoRepo = TrackInfoRepoImpl()
    val getTrackInfoUseCase = GetTrackInfoUseCase(trackInfoRepo)

    private val playlistsRepo: PlaylistsRepo = PlaylistsRepoImpl()
    val getPlaylistsUseCase = GetPlaylistsUseCase(playlistsRepo)
    val insertPlaylistUseCase = InsertPlaylistUseCase(playlistsRepo)
    val deletePlaylistUseCase = DeletePlaylistUseCase(playlistsRepo)

    private val tracksRepo: TracksRepo = TracksRepoImpl()
    val getTracksListUseCase = GetTracksListUseCase(tracksRepo)
    val insertTrackUseCase = InsertTrackUseCase(tracksRepo)
    val deleteTrackUseCase = DeleteTrackUseCase(tracksRepo)
}