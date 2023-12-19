package com.example.musicapp.domain.useCases.tracks

import android.util.Log
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetTracksListUseCase @Inject constructor(val repo: TracksRepo) {

    fun getPlaylistTracksList(
        playlistId: Int,
        callback: (List<MusicTrack>) -> Unit
    ) {
        repo.getTracksIdsInSinglePlaylist(playlistId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { list ->
                    repo.getTracksList(list, callback)
                },
                { error ->
                    Log.e("RxJava", "getPlaylistTracksList fun problem: $error")
                }
            )
    }

    fun getSearchResults(
        queryText: String
    ): Single<Music> {
        return repo.getSearchResult(queryText)
    }

    fun lookForTrackInPlaylist(
        trackId: Long,
        playlistId: Int
    ): Single<List<Long>> {
        return repo.findTrackInSinglePlaylist(trackId, playlistId)
    }

    fun lookForTrackInMedia(
        trackId: Long
    ): Single<List<Long>> {
        return repo.lookForTrackInMedia(trackId)
    }
}