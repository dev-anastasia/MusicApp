package com.example.musicapp.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PlaylistEntity::class,
        TrackEntity::class,
        PlaylistTrackCrossRef::class
    ], version = 1
)
abstract class PlaylistDatabase : RoomDatabase() {

    abstract fun playlistsDao(): PlaylistsDao

    companion object {
        @Volatile   // Чтобы избежать рассинхрона в разных потоках
        private var DB_INSTANCE: PlaylistDatabase? = null

        fun getDatabase(context: Context): PlaylistDatabase {
            if (DB_INSTANCE == null) {
                val db = Room.databaseBuilder(
                    context,
                    PlaylistDatabase::class.java,
                    "database-playlists-and-favs"
                ).build()
                DB_INSTANCE = db
            }
            return DB_INSTANCE!!
        }
    }
}