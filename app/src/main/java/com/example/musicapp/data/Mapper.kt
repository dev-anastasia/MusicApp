package com.example.musicapp.data

import com.example.musicapp.domain.database.PlaylistEntity
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.Playlist

class Mapper {
    // Конвертирует сущности базы данных в дата-классы для domain и ui слоёв, и наоборот

    fun trackTableListToMusicTrackList(list: List<TrackEntity>): List<MusicTrack> {
        val listOfTracks = mutableListOf<MusicTrack>()
        for (i in list.indices) {
            listOfTracks.add(MusicTrack(
                list[i].trackId,
                list[i].artistName,
                list[i].trackName,
                null,
                list[i].artworkUrl60,
                null
            ))
        }
        return listOfTracks
    }

    fun musicTrackToTrackTable(track: MusicTrack): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.artistName,
            track.trackName,
            track.artworkUrl60!!,
            System.currentTimeMillis()
        )
    }

    fun playlistTableListToPlaylistList(list: List<PlaylistEntity>): List<Playlist> {
        val playlistList = mutableListOf<Playlist>()
        for (i in list.indices) {
            playlistList.add(
                Playlist(
                    list[i].playlistId,
                    list[i].playlistName,
                    list[i].playlistCover,
                    list[i].systemTimeMillis
                )
            )
        }
        return playlistList
    }

    fun playlistToPlaylistTable(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistCover,
            playlist.systemTimeMillis
        )
    }
}