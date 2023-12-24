package com.example.musicapp.dagger.modules

import com.example.musicapp.application.MainApp
import com.example.musicapp.domain.database.MyDao
import com.example.musicapp.domain.database.MyDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(app: MainApp): MyDatabase {
        return MyDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun providesDao(db: MyDatabase): MyDao {
        return db.dao()
    }
}