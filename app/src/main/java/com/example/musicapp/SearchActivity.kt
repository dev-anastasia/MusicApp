package com.example.musicapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.adapter.ItemDiffUtilCallback
import com.example.musicapp.adapter.MusicAdapter
import com.example.musicapp.data.Music
import com.example.musicapp.data.MusicPiece
import com.example.musicapp.network.MusicService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    var currentQueryText: String? = ""  // текущий текст запроса
    private lateinit var musicAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val uiHandler = Handler(Looper.getMainLooper())
        val errorText: TextView = findViewById(R.id.search_activity_tv_error)
        val loadingImage: ImageView =
            findViewById(R.id.search_activity_iv_loading) // Иконка загрузки
        loadingImage.visibility = GONE
        val errorLayout: FrameLayout =
            findViewById(R.id.search_activity_fl_error) // Картинка + сообщение о проблеме
        errorLayout.visibility = GONE

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.search_activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        val emptyList = emptyList<MusicPiece>()
        musicAdapter =
            MusicAdapter(emptyList) // изначально список пустой для корректной работы DiffUtil
        recyclerView.adapter = musicAdapter

        // Retrofit + Перехватчик (Interceptor)
        val interceptor = HttpLoggingInterceptor()         // logs request and response information
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        // it's best to use a single OkHttpClient instance and reuse it for all HTTP calls
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val musicService = retrofit.create(MusicService::class.java)


        fun showNoSuchResult() {    // Сообщение при 0 найденных результатов
            errorLayout.visibility = VISIBLE
            loadingImage.visibility = GONE
            errorText.text = "Ничего не найдено :("
        }

        fun showSomeSearchProblem() {   // Сообщение о любой ошибке
            errorLayout.visibility = VISIBLE
            loadingImage.visibility = GONE
            errorText.text = "Непредвиденная ошибка"
        }

        fun showLoading() {     // Иконка загрузки
            loadingImage.visibility = VISIBLE
            errorLayout.visibility = GONE
        }

        fun hideKeyboard() {    // При общении с сервером убирает клавиатуру
            val view: View? = this.currentFocus
            val inputMethodManager = getSystemService(InputMethodManager::class.java)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        fun calculateDiff(newList: List<MusicPiece>) {
            val diffUtil = DiffUtil.calculateDiff(ItemDiffUtilCallback(musicAdapter.list, newList))
            musicAdapter = MusicAdapter(newList)
            diffUtil.dispatchUpdatesTo(musicAdapter)
            recyclerView.adapter = musicAdapter
        }

        // запрос SearchView
        val searchQueryRunnable = Runnable {

            showLoading() // Не отображает, не могу понять почему...

            if (currentQueryText == null)
                currentQueryText = ""
            val searchObject: Call<Music> = musicService.getSearchResult(currentQueryText!!, ENTITY)

            searchObject.enqueue(object : Callback<Music> {

                override fun onResponse(call: Call<Music>, response: Response<Music>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.resultCount != 0) {  // если не 0 результатов поиска
                            val newList = response.body()!!.results
                            calculateDiff(newList)
                        } else {
                            // Очистка списка для корректного отображения "Ничего не найдено":
                            val newList = emptyList<MusicPiece>()
                            calculateDiff(newList)
                            // Чтобы не выдавал "Ничего не найдено" при пустом поисковике:
                            if (currentQueryText?.isEmpty()!!.not())
                                showNoSuchResult()
                        }

                    } else {               // если Response не был Successful
                        // Очистка списка для корректного отображения сообщения об ошибке:
                        val newList = emptyList<MusicPiece>()
                        calculateDiff(newList)
                        showSomeSearchProblem()
                    }
                }

                override fun onFailure(call: Call<Music>, t: Throwable) {
                    // Очистка списка для корректного отображения сообщения об ошибке:
                    val newList = emptyList<MusicPiece>()
                    calculateDiff(newList)
                    showSomeSearchProblem()
                }
            })

            loadingImage.visibility = GONE // Если убрать эту строку, иконка отображается когда надо
            hideKeyboard()
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

        findViewById<ImageView>(R.id.search_activity_btn_go_back).setOnClickListener {
            finish()
        }
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val ENTITY = "musicTrack"   // для поиска только музыкальных треков
        const val TIMER = 2000L
    }
}