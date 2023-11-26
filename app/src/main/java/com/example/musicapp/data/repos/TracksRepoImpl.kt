package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.data.Mapper
import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.PlaylistDatabase
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.TracksList
import com.example.musicapp.presentation.presenters.PlayerViewModel
import retrofit2.Call

class TracksRepoImpl : TracksRepo {

    private val mapper = Mapper()

    override fun getTrackInfo(
        currentId: Long,
        context: Context,
        callback: (HashMap<String, String>) -> Unit
    ) {

        val mapOfSpecs = HashMap<String, String>()

        Thread {
            val searchObject: Call<TracksList> =
                RetrofitUtils.musicService.getTrackInfoById(currentId)
            val responseBody = searchObject.execute().body()

            if (responseBody != null) {
                if (responseBody.resultCount != 0) {
                    val res = responseBody.results[0]
                    mapOfSpecs[PlayerViewModel.ARTIST_NAME] = res.artistName
                    mapOfSpecs[PlayerViewModel.TRACK_NAME] = res.trackName
                    mapOfSpecs[PlayerViewModel.DURATION] = res.trackTimeMillis.toString()
                    mapOfSpecs[PlayerViewModel.PREVIEW] = res.previewUrl
                    mapOfSpecs[PlayerViewModel.COVER_IMAGE_100] = res.artworkUrl100
                    mapOfSpecs[PlayerViewModel.COVER_IMAGE_60] = res.artworkUrl60
                }
            }   // В остальных случаях вернётся пустая мапа
            callback(mapOfSpecs)
        }.start()
    }

    override fun getTracksIdsInSinglePlaylist(
        playlistId: Int,
        context: Context
    ): List<Long> {
        return PlaylistDatabase.getDatabase(context).dao().getTracksIds(playlistId)
    }

    override fun getTracksList(
        trackIdsList: List<Long>,
        context: Context,
        callback: (List<MusicTrack>) -> Unit
    ) {
        val list = mutableListOf<TrackEntity>()
        for (i in trackIdsList) {
            list.add(PlaylistDatabase.getDatabase(context).dao().getAllTracksListById(i))
        }
        val result = mapper.trackTableListToMusicTrackList(list)
        callback(result)
    }

    override fun getSearchResult(
        queryText: String,
        entity: String,
        callback: (List<MusicTrack>) -> Unit
    ) {

        var responseBody: Music?

        val thread = Thread {
            val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
                queryText,
                entity
            )
            responseBody = searchObject.execute().body()
            var res = emptyList<MusicTrack>()

            if (responseBody != null) {
                if (responseBody!!.resultCount != 0)
                    res = responseBody!!.results
            }
            callback(res)
        }
        thread.start()
    }

    override fun addTrackInPlaylist(
        track: MusicTrack,
        playlistId: Int,
        context: Context
    ) {
        val trackTable = mapper.musicTrackToTrackTable(track)
        PlaylistDatabase.getDatabase(context).dao().addTrackToPlaylist(
            PlaylistTrackCrossRef(playlistId, track.trackId), trackTable
        )
    }


    override fun findTrackInSinglePlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        val res = PlaylistDatabase.getDatabase(context).dao()
            .findTrackInSinglePlaylist(playlistId, trackId)
        callback(res)
    }

    override fun getPlaylistsOfThisTrack(
        trackId: Long,
        context: Context,
        callback: (List<Int>) -> Unit
    ) {
        val res = PlaylistDatabase.getDatabase(context).dao()
            .getPlaylistsOfThisTrack(trackId)
        callback(res)
    }

    override fun findTrackInMedia(
        trackId: Long,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        val res = PlaylistDatabase.getDatabase(context).dao()
            .lookForTrackInPlaylists(trackId)
        callback(res)
    }

    override fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context
    ) {
        PlaylistDatabase.getDatabase(context).dao().deleteTrackFromPlaylist(
            PlaylistTrackCrossRef(playlistId, trackId), trackId
        )
    }
}