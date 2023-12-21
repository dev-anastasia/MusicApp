package com.example.musicapp.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlaylistEntity::class, TrackEntity::class, PlaylistTrackCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun dao(): MyDao

    companion object {

        @Volatile   // Чтобы избежать рассинхрона в разных потоках
        private var DB_INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            if (DB_INSTANCE == null) {
                val db = Room.databaseBuilder(
                    context,
                    MyDatabase::class.java,
                    "database-playlists-and-favs"
                ).build()
                DB_INSTANCE = db
            }
            return DB_INSTANCE!!
        }
    }
}