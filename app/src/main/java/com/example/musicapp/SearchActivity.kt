package com.example.musicapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.adapter.MusicAdapter
import com.example.musicapp.data.Music
import com.example.musicapp.network.MusicService
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    var currentQueryText: String? = ""
    private lateinit var musicAdapter: MusicAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val uiHandler = Handler(Looper.getMainLooper())
        val errorText: TextView = findViewById(R.id.search_activity_tv_error)
        val errorImage: ImageView = findViewById(R.id.search_activity_iv_error)
        val loadingImage: ImageView = findViewById(R.id.search_activity_iv_loading)
        loadingImage.visibility = GONE

        fun showNoSuchResult() {
            findViewById<FrameLayout>(R.id.search_activity_fl_error).visibility = VISIBLE
            loadingImage.visibility = GONE
            Picasso.get()
                .load(R.drawable.lion)
                .into(errorImage)
            errorText.text = "Ничего не найдено :("
        }

        fun showSomeSearchProblem() {
            findViewById<FrameLayout>(R.id.search_activity_fl_error).visibility = VISIBLE
            loadingImage.visibility = GONE
            Picasso.get()
                .load(R.drawable.lion)
                .into(errorImage)
            errorText.text = "Возникла какая-то проблема"
        }

        fun showLoading() {
            loadingImage.visibility = VISIBLE
        }

        // Retrofit + Перехватчик (Interceptor)
        val interceptor = HttpLoggingInterceptor() // logs request and response information
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        // it's best to use a single OkHttpClient instance and reuse it for all HTTP calls
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val musicService = retrofit.create(MusicService::class.java)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.search_activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // SearchView
        val searchQuery = Runnable {

            findViewById<FrameLayout>(R.id.search_activity_fl_error).visibility = GONE
            showLoading()

            val searchObject: Call<Music> = (
                    if (currentQueryText != null)
                        musicService.getSearchResult(currentQueryText!!)
                    else
                        musicService.getSearchResult("")
                    )

            searchObject.enqueue(object : Callback<Music> {
                override fun onResponse(call: Call<Music>, response: Response<Music>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.resultCount != 0) {
                            val list = response.body()!!.results
                            musicAdapter = MusicAdapter(list)
                            recyclerView.adapter = musicAdapter
                        } else {
                            if (currentQueryText != null)
                                showNoSuchResult()
                        }
                    } else
                        showSomeSearchProblem()
                }

                override fun onFailure(call: Call<Music>, t: Throwable) {
                    showSomeSearchProblem()
                }
            })

            loadingImage.visibility = GONE
        }

        findViewById<SearchView>(R.id.search_activity_search_view).setOnQueryTextListener( // требует listener'а
            object : SearchView.OnQueryTextListener { // тот самый требуемый listener

                override fun onQueryTextSubmit(query: String?): Boolean {
                    uiHandler.removeCallbacks(searchQuery)
                    uiHandler.post(searchQuery)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    uiHandler.removeCallbacks(searchQuery)
                    currentQueryText = newText
                    uiHandler.postDelayed(searchQuery, TIMER)
                    return true
                }
            }
        )
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val TIMER = 2000L
    }
}