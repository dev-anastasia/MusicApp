package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import io.reactivex.Single
import javax.inject.Inject

class GetPlaylistInfoUseCase @Inject constructor(val repo: PlaylistsRepo) {

    fun getPlaylistTracksCount(playlistId: Int): Single<List<Long>> {
        return repo.getPlaylistTracksCount(playlistId)
    }

    fun getTrackIdCover(trackId: Long): Single<String> {
        return repo.getTrackCover(trackId)
    }
}