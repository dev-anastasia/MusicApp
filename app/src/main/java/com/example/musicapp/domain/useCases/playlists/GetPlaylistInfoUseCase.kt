package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import io.reactivex.Single

class GetPlaylistInfoUseCase(private var repo: PlaylistsRepo) {

    fun getPlaylistTrackCount(playlistId: Int): Single<List<Long>> {
        return repo.getPlaylistTracksCount(playlistId)
    }

    fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit) {
        repo.getPlaylistCover(playlistId) {
            callback(it)
        }
    }
}