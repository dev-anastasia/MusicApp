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
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TracksRepoImpl : TracksRepo {

    private val mapper = Mapper()

    override fun getTrackInfo(
        currentId: Long,
        context: Context
    ): Single<TracksList> {
        return RetrofitUtils.musicService.getTrackInfoById(currentId)
            .subscribeOn(Schedulers.io())
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
        val result = mapper.trackEntityListToMusicTrackList(list)
        callback(result)
    }

    override fun getSearchResult(
        queryText: String,
        entity: String
    ): Single<Music> {

        return RetrofitUtils.musicService.getSearchResult(
            queryText,
            entity
        ).subscribeOn(Schedulers.io())
    }

    override fun addTrackInPlaylist(
        track: MusicTrack,
        playlistId: Int,
        context: Context
    ) {
        val trackTable = mapper.musicTrackToTrackEntity(track)
        PlaylistDatabase.getDatabase(context).dao().addTrackToPlaylist(
            PlaylistTrackCrossRef(playlistId, track.trackId), trackTable
        )
    }


    override fun findTrackInSinglePlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context
    ): List<Long> {
        return PlaylistDatabase.getDatabase(context).dao()
            .findTrackInSinglePlaylist(playlistId, trackId)
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