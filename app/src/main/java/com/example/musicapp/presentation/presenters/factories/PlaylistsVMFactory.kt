package com.example.musicapp.presentation.presenters.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.domain.useCases.playlists.DeletePlaylistUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistInfoUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.playlists.InsertPlaylistUseCase
import com.example.musicapp.presentation.presenters.PlaylistsViewModel
import javax.inject.Inject

class PlaylistsVMFactory @Inject constructor(
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val insertPlaylistUseCase: InsertPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getPlaylistInfoUseCase: GetPlaylistInfoUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistsViewModel(
            getPlaylistsUseCase,
            insertPlaylistUseCase,
            deletePlaylistUseCase,
            getPlaylistInfoUseCase
        ) as T
    }
}