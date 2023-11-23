package com.example.musicapp.domain.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracks(      // не используется, не разобралась как с такими классами работать...
    @Embedded val playlist: PlaylistTable,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    ) val tracks: List<TrackTable>
)
