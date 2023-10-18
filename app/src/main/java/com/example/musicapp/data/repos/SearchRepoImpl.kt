package com.example.musicapp.data.repos

import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicPiece
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepoImpl : SearchRepo {

    override fun getSearchResult(
        queryText: String,
        entity: String,
        dataListener: (List<MusicPiece>) -> Unit
    ) {
        val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
            queryText,
            entity
        )

        searchObject.enqueue(object : Callback<Music> {

            override fun onResponse(call: Call<Music>, response: Response<Music>) {
                if (response.isSuccessful) {
                    if (response.body()!!.resultCount != 0) {  // если есть результаты поиска
                        dataListener(response.body()!!.results)
                    }
                }
            }

            override fun onFailure(call: Call<Music>, t: Throwable) {
            }
        })
    }
}