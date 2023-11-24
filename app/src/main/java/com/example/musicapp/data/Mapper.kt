package com.example.musicapp.data

import com.example.musicapp.domain.database.PlaylistTable
import com.example.musicapp.domain.entities.Playlist

class Mapper {

    fun trackTableToTrack() {

    }

    fun trackToTrackTable() {

    }

    fun playlistTableListToPlaylistList(list: List<PlaylistTable>): List<Playlist> {
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

    fun playlistToPlaylistTable() {

    }
}