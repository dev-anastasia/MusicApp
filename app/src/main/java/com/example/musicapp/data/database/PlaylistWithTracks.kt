package com.example.musicapp.data.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

//data class PlaylistWithTracks(
//    @Embedded val playlist: PlaylistEntity,
//    @Relation(
//        parentColumn = "playlistId",
//        entityColumn = "trackId",
//        associateBy = Junction(PlaylistTrackCrossRef::class)
//    )
//    val tracks: List<TrackEntity>
//)
