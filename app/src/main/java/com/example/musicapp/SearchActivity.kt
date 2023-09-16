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
        val loadingImage: ImageView = findViewById(R.id.search_activity_iv_loading)
        loadingImage.visibility = GONE
        val errorLayout: FrameLayout = findViewById(R.id.search_activity_fl_error)
        errorLayout.visibility = GONE

        fun showNoSuchResult() {
            errorLayout.visibility = VISIBLE
            loadingImage.visibility = GONE
            errorText.text = "Ничего не найдено :("
        }

        fun showSomeSearchProblem() {
            errorLayout.visibility = VISIBLE
            loadingImage.visibility = GONE
            errorText.text = "Непредвиденная ошибка"
        }

        fun showLoading() {
            loadingImage.visibility = VISIBLE
            errorLayout.visibility = GONE
        }

        // Retrofit + Перехватчик (Interceptor)
        val interceptor =
            HttpLoggingInterceptor()             // logs request and response information
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
        val searchQueryRunnable = Runnable {
            errorLayout.visibility = GONE
            showLoading()

            if (currentQueryText == null)
                currentQueryText = ""
            val searchObject: Call<Music> = musicService.getSearchResult(currentQueryText!!)

            searchObject.enqueue(object : Callback<Music> {

                override fun onResponse(call: Call<Music>, response: Response<Music>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.resultCount != 0) {
                            val list = response.body()!!.results
                            musicAdapter = MusicAdapter(list)
                            recyclerView.adapter = musicAdapter
                        } else {                                   // если 0 результатов поиска
                            if (currentQueryText?.isEmpty()!!.not()) // Чтобы не выводил картинку при пустом поисковике
                                showNoSuchResult()
                        }
                    } else                                         // если Response.isSuccessful.not()
                        showSomeSearchProblem()
                }

                override fun onFailure(call: Call<Music>, t: Throwable) {
                    showSomeSearchProblem()
                }
            })

            loadingImage.visibility = GONE
        }

        findViewById<SearchView>(R.id.search_activity_search_view).setOnQueryTextListener( // требует listener'а
            object : SearchView.OnQueryTextListener {               // тот самый требуемый listener

                override fun onQueryTextSubmit(query: String?): Boolean {
                    uiHandler.removeCallbacks(searchQueryRunnable)
                    uiHandler.post(searchQueryRunnable)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    uiHandler.removeCallbacks(searchQueryRunnable)
                    currentQueryText = newText
                    uiHandler.postDelayed(searchQueryRunnable, TIMER)
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