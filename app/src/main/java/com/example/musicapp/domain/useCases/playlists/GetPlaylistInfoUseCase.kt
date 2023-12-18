package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo

class GetPlaylistInfoUseCase(val repo: PlaylistsRepo) {

    fun getPlaylistTrackCount(playlistId: Int): List<Long> {
        return repo.getPlaylistTracksCount(playlistId)
    }

    fun getPlaylistCover(playlistId: Int): String? {
        return repo.getPlaylistCover(playlistId)
    }
}