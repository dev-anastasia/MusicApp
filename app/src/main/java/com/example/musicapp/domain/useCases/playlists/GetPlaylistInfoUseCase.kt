package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.domain.TracksRepo

class GetPlaylistInfoUseCase(private var repo: TracksRepo) {

    fun getPlaylistTrackCount(playlistId: Int, context: Context): List<Long> {
        return repo.getTracksIdsInSinglePlaylist(context, playlistId)
    }

    fun getPlaylistCover(playlistId: Int, context: Context): String {
        return repo.getPlaylistCover(context, playlistId)
    }
}