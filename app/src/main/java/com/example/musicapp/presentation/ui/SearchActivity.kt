package com.example.musicapp.presentation.ui

import android.content.Intent
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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.presentation.OnItemClickListener
import com.example.musicapp.presentation.presenters.SearchViewModel
import com.example.musicapp.presentation.ui.adapter.MusicAdapter

class SearchActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var searchQueryRunnable: Runnable
    private lateinit var musicAdapter: MusicAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Creator.searchVM = viewModel

        val uiHandler = Handler(Looper.getMainLooper())
        var currentQueryText: String? = ""  // текущий текст запроса

        val errorText: TextView = findViewById(R.id.search_activity_tv_error)
        // Иконка загрузки
        val loadingImage: ImageView = findViewById(R.id.search_activity_iv_loading)
        loadingImage.visibility = GONE
        // Картинка + сообщение о проблеме:
        val errorLayout: FrameLayout = findViewById(R.id.search_activity_fl_error)
        errorLayout.visibility = GONE

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.search_activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        musicAdapter = MusicAdapter(this)
        viewModel.initList() // Создает пустой список, если в нем null

        viewModel.newList.observe(this) { list ->
            musicAdapter.updateList(list)
            recyclerView.adapter = musicAdapter
        }

        // Методы активити
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

        fun hideKeyboard() {    // При работе с сервером убирает клавиатуру
            val view: View? = this.currentFocus
            val inputMethodManager = getSystemService(InputMethodManager::class.java)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        //fun calculateDiff() {
//            if (searchViewModel.newListLiveData.value == null)
//                searchViewModel.newListLiveData.value = emptyList()
//
//            val diffUtil = DiffUtil.calculateDiff(
//                ItemDiffUtilCallback(
//                    searchViewModel.oldList,
//                    searchViewModel.newListLiveData.value!!
//                )
//            )
//            musicAdapter = MusicAdapter(this)
//            diffUtil.dispatchUpdatesTo(musicAdapter)

//            recyclerView.adapter = musicAdapter
        //}

        // запрос SearchView
        searchQueryRunnable = Runnable {

            showLoading()

            if (currentQueryText == null)
                currentQueryText = ""

            // Оповещаем посредника о клике
            viewModel.onGetTrackListClicked(currentQueryText!!, ENTITY)

            //calculateDiff()

            loadingImage.visibility = GONE
            if (currentQueryText?.isEmpty()!!.not())
                hideKeyboard()
        }

        findViewById<SearchView>(R.id.search_activity_search_view).setOnQueryTextListener( // требует listener

            object : SearchView.OnQueryTextListener {               // тот самый требуемый listener

                override fun onQueryTextSubmit(query: String?): Boolean {
                    uiHandler.apply {
                        removeCallbacks(searchQueryRunnable)
                        post(searchQueryRunnable)
                    }
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

        findViewById<ImageView>(R.id.player_activity_btn_go_back).setOnClickListener {
            finish()
        }
    }

    override fun onItemClick(id: Long) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(TRACK_ID, id)
        this.startActivity(intent)
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val TRACK_ID = "track id key"
        const val ENTITY = "musicTrack"   // для поиска только музыкальных треков
        const val TIMER = 2000L
    }
}