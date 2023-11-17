package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.PlaylistDatabase
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.PlaylistWithTracks
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicPiece
import retrofit2.Call

class TracksRepoImpl : TracksRepo {

    override fun getTracksIds(
        context: Context,
        playlistId: Int
    ): List<Long> {
        return PlaylistDatabase.getDatabase(context).playlistsDao().getTracksIds(playlistId)
    }

    override fun getTracksInPlaylist(
        context: Context,
        trackId: Long,
        callback: (List<TrackEntity>) -> Unit
    ) {
        val res = PlaylistDatabase.getDatabase(context).playlistsDao()
            .getAllTracksListById(trackId)
        callback(res)
    }

    override fun getTracksList(
        context: Context,
        trackIdsList: List<Long>,
        callback: (List<TrackEntity>) -> Unit
    ) {
        for (i in trackIdsList) {
            callback(PlaylistDatabase.getDatabase(context).playlistsDao().getAllTracksListById(i))
        }
    }

    override fun getSearchResult(
        queryText: String,
        entity: String,
        callback: (List<MusicPiece>) -> Unit
    ) {

        var responseBody: Music?

        val thread = Thread {
            val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
                queryText,
                entity
            )
            responseBody = searchObject.execute().body()
            var res = emptyList<MusicPiece>()

            if (responseBody != null) {
                if (responseBody!!.resultCount != 0)
                    res = responseBody!!.results
            }
            callback(res)
        }
        thread.start()
    }

    override fun addPlaylistTrackRef(
        track: TrackEntity,
        ref: PlaylistTrackCrossRef,
        context: Context
    ) {
        PlaylistDatabase.getDatabase(context).playlistsDao().addPlaylistTrackRef(ref)
        PlaylistDatabase.getDatabase(context).playlistsDao().addTrackToDB(track)
    }

    override fun findTrackInDB(
        playlistId: Int,
        trackId: Long,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        val res =
            PlaylistDatabase.getDatabase(context).playlistsDao().findTrackInDB(playlistId, trackId)
        callback(res)
    }
}