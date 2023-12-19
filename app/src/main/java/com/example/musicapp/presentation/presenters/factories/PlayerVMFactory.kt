package com.example.musicapp.presentation.presenters.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.tracks.DeleteTrackUseCase
import com.example.musicapp.domain.useCases.tracks.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.domain.useCases.tracks.InsertTrackUseCase
import com.example.musicapp.presentation.presenters.PlayerViewModel
import javax.inject.Inject

class PlayerVMFactory @Inject constructor(
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val getTracksListUseCase: GetTracksListUseCase,
    private val insertTrackUseCase: InsertTrackUseCase,
    private val deleteTrackUseCase: DeleteTrackUseCase,
    private val getTrackInfoUseCase: GetTrackInfoUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            getPlaylistsUseCase,
            getTracksListUseCase,
            insertTrackUseCase,
            deleteTrackUseCase,
            getTrackInfoUseCase
        ) as T
    }
}