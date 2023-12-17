package com.example.musicapp.data.repos

import com.example.musicapp.Creator
import com.example.musicapp.data.Mapper
import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.TracksList
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TracksRepoImpl : TracksRepo {

    private val mapper = Mapper()

    override fun getTrackInfo(
        currentId: Long
    ): Single<TracksList> {
        return RetrofitUtils.musicService.getTrackInfoById(currentId)
            .subscribeOn(Schedulers.io())
    }

    override fun getTracksIdsInSinglePlaylist(
        playlistId: Int
    ): Single<List<Long>> {
        return Creator.dao.getTracksIdsSingle(playlistId)
    }

    override fun getTracksList(
        trackIdsList: List<Long>, callback: (List<MusicTrack>) -> Unit
    ) {
        val list = mutableListOf<TrackEntity>()
        for (i in trackIdsList) {
            list.add(Creator.dao.getAllTracksListById(i))
        }

        val result = mapper.trackEntityListToMusicTrackList(list)
        callback(result)
    }

    override fun getSearchResult(
        queryText: String
    ): Single<Music> {
        return RetrofitUtils.musicService.getSearchResult(queryText, ENTITY)
            .subscribeOn(Schedulers.io())
    }

    override fun addTrackInPlaylist(
        track: MusicTrack, playlistId: Int
    ) {
        val trackTable = mapper.musicTrackToTrackEntity(track)
        Creator.dao.addTrackToPlaylist(trackTable, playlistId)
    }

    override fun findTrackInSinglePlaylist(
        trackId: Long, playlistId: Int
    ): Single<List<Long>> {
        return Creator.dao.findTrackInSinglePlaylist(playlistId, trackId)
            .subscribeOn(Schedulers.io())
    }

    override fun getPlaylistsOfThisTrack(
        trackId: Long, callback: (List<Int>) -> Unit
    ) {
        val res = Creator.dao.getPlaylistsOfThisTrack(trackId)
        callback(res)
    }

    override fun lookForTrackInMedia(
        trackId: Long
    ): Single<List<Long>> {
        return Creator.dao.lookForTrackInPlaylists(trackId)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteTrackFromPlaylist(
        trackId: Long, playlistId: Int
    ) {
        Creator.dao.deleteTrackFromPlaylist(playlistId, trackId)
    }

    private companion object {
        const val ENTITY = "musicTrack"   // для поиска только музыкальных треков
    }
}