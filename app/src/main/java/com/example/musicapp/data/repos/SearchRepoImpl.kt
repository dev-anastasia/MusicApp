package com.example.musicapp.data.repos

import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.data.network.WorkerThread
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicPiece
import retrofit2.Call

class SearchRepoImpl : SearchRepo {

    //private val workerThread = WorkerThread()

    override fun getSearchResult(
        queryText: String,
        entity: String
    ): List<MusicPiece> {

        lateinit var responseBody: Music

        val thread = Thread {
            val searchObject: Call<Music> = RetrofitUtils.musicService.getSearchResult(
                queryText,
                entity
            )
            responseBody = searchObject.execute().body()!!
        }
        thread.apply {
            start()
            join()
        }

        return if (responseBody.resultCount != 0)
            responseBody.results
        else
            emptyList()

//        searchObject.enqueue(object : Callback<Music> {
//
//            override fun onResponse(call: Call<Music>, response: Response<Music>) {
//                if (response.isSuccessful) {
//                    if (response.body()!!.resultCount != 0) {  // если есть результаты поиска
//                        dataListener(response.body()!!.results)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Music>, t: Throwable) {
//            }
//        })
    }
}