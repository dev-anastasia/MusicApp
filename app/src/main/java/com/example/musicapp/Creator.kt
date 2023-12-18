package com.example.musicapp

import android.media.MediaPlayer
import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.data.repos.TracksRepoImpl
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.MyDao
import com.example.musicapp.domain.player.PlayerClassImpl
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.tracks.DeleteTrackUseCase
import com.example.musicapp.domain.useCases.tracks.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.domain.useCases.tracks.InsertTrackUseCase
import com.example.musicapp.presentation.PlayerClass

object Creator {

    val dao: MyDao
        get() {
            return _dao!!
        }
    var _dao: MyDao? = null     // Объект создаётся при входе в приложение, в MainActivity

    private val playlistsRepo: PlaylistsRepo = PlaylistsRepoImpl()
    val getPlaylistsUseCase = GetPlaylistsUseCase(playlistsRepo)

    private val tracksRepo: TracksRepo = TracksRepoImpl()
    val getTracksListUseCase = GetTracksListUseCase(tracksRepo)
    val insertTrackUseCase = InsertTrackUseCase(tracksRepo)
    val deleteTrackUseCase = DeleteTrackUseCase(tracksRepo)
    val getTrackInfoUseCase = GetTrackInfoUseCase(tracksRepo)

    val playerClass: PlayerClass = PlayerClassImpl()

    var mediaPlayer = MediaPlayer()
}