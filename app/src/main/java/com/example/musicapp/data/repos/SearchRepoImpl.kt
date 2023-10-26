package com.example.musicapp.data.repos

import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicPiece
import retrofit2.Call
import retrofit2.Response

class SearchRepoImpl : SearchRepo {

    override fun getSearchResult(
        queryText: String,
        entity: String
    ): List<MusicPiece> {

        lateinit var response: Response<Music>
        lateinit var body: Music

        val thread = Thread {
            val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
                queryText,
                entity
            )
            response = searchObject.execute()
            body = response.body()!!
        }
        thread.apply {
            start()
            join()
        }

        return if (body.resultCount != 0)
            body.results
        else
            emptyList()
    }
}