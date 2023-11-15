package com.example.musicapp.data.repos

import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicPiece
import retrofit2.Call

class SearchRepoImpl : SearchRepo {

    override fun getSearchResult(
        queryText: String,
        entity: String,
        callback: (List<MusicPiece>) -> Unit
    ) {

        var responseBody: Music?

        val thread = Thread {
            val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
                queryText,
                entity
            )
            responseBody = searchObject.execute().body()
            var res = emptyList<MusicPiece>()

            if (responseBody != null) {
                if (responseBody!!.resultCount != 0)
                    res = responseBody!!.results
            }
            callback(res)
        }
        thread.start()
    }
}