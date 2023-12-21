package com.example.musicapp.data.repos

import android.util.Log
import com.example.musicapp.data.Mapper
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.database.MyDao
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlaylistsRepoImpl @Inject constructor(private val dao: MyDao) : PlaylistsRepo {

    private val mapper = Mapper()

    override fun getPlaylistTracksCount(
        playlistId: Int
    ): Single<List<Long>> {
        return dao.getTracksIdsList(playlistId)
            .subscribeOn(Schedulers.io())
    }

    override fun getTrackCover(trackId: Long): Single<String> {
        return dao.getTrackCoverSingle(trackId)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllPlaylists(): Single<List<Playlist>> {
        return dao.getAllPlaylists()
            .subscribeOn(Schedulers.io())
            .map { mapper.playlistEntityListToPlaylistList(it) }
    }

    override fun insertPlaylist(playlist: Playlist) {
        val playlistTable = mapper.playlistToPlaylistEntity(playlist)
        dao.insertPlaylist(playlistTable)
    }

    override fun deletePlaylist(id: Int) {
        dao.getTracksIdsList(id)
            .subscribe(
                { list ->
                    for (trackId in list) {
                        dao.deleteTrackFromPlaylist(id, trackId)
                    }
                    // Удаляем плелист из БД
                    dao.deletePlaylist(id)
                },
                { error ->
                    Log.e("RxJava", "fun deletePlaylist problem: $error")
                }
            )
    }
}
