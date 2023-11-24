package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.presentation.PlaylistsRepo

class GetPlaylistInfoUseCase(private var repo: PlaylistsRepo) {

    fun getPlaylistTrackCount(playlistId: Int, context: Context, callback: (Int) -> Unit) {
        repo.getPlaylistTracksCount(playlistId, context) {
            callback(it)
        }
    }

    fun getPlaylistCover(playlistId: Int, context: Context, callback: (String) -> Unit) {
        repo.getPlaylistCover(context, playlistId) {
            callback(it)
        }
    }
}