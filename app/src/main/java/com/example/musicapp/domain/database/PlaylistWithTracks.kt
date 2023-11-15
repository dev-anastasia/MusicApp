package com.example.musicapp.domain.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// УДАЛИТЬ ?

//data class PlaylistWithTracks(
//    @Embedded val playlist: PlaylistEntity,
//    @Relation(
//        parentColumn = "playlistId",
//        entityColumn = "trackId",
//        associateBy = Junction(PlaylistTrackCrossRef::class)
//    )
//    val tracks: List<TrackEntity>
//)
