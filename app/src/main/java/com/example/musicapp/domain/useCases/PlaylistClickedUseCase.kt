package com.example.musicapp.domain.useCases

import android.content.Context
import com.example.musicapp.domain.PlaylistsResultListener
import com.example.musicapp.domain.entities.database.PlaylistEntity
import com.example.musicapp.presentation.PlaylistsRepo

class PlaylistClickedUseCase(
    private var repo: PlaylistsRepo,
    private var vm: PlaylistsResultListener? = null  // Пока что не используется
) {

    fun addPlaylist(context: Context, playlist: PlaylistEntity) {
        repo.addPlaylist(context, playlist)
    }

    fun deletePlaylist(context: Context, id: Int) {
        repo.deletePlaylist(context, id)
    }

    fun getAllPlaylists(context: Context): List<PlaylistEntity> {
        return repo.getAllPlaylists(context)
    }

    fun setVM(vm: PlaylistsResultListener) {
        this.vm = vm
    }
}