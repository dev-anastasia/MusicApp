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
        var responseBody: Music? = null

        val thread = Thread {
            val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
                queryText,
                entity
            )
            response = searchObject.execute()
            responseBody = response.body()
        }
        thread.apply {
            start()
            join()
        }

        return if (responseBody != null) {
            if (responseBody!!.resultCount != 0)
                responseBody!!.results
            else
                emptyList()
        } else
            emptyList()
    }
}