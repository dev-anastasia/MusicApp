package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.presentation.OnItemClickListener
import com.example.musicapp.presentation.presenters.SearchViewModel
import com.example.musicapp.presentation.ui.PlayerActivity
import com.example.musicapp.presentation.ui.SearchActivity
import com.example.musicapp.presentation.ui.adapter.MusicAdapter

class SearchFragment : Fragment(R.layout.fragment_search), OnItemClickListener {

    private lateinit var searchQueryRunnable: Runnable
    private lateinit var musicAdapter: MusicAdapter
    private val vm: SearchViewModel by viewModels()

    override fun onItemClick(id: Long) {
//        val intent = Intent(this, PlayerActivity::class.java)
//        intent.putExtra(SearchActivity.TRACK_ID, id)
//        this.startActivity(intent)
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val uiHandler = Handler(Looper.getMainLooper())
//        var currentQueryText: String? = ""  // текущий текст запроса
//
//        val errorText: TextView = findViewById(R.id.search_activity_tv_error)
//        // Иконка загрузки
//        val loadingImage: ImageView = findViewById(R.id.search_activity_iv_loading)
//        loadingImage.visibility = View.GONE
//        // Картинка + сообщение о проблеме:
//        val errorLayout: FrameLayout = findViewById(R.id.search_activity_fl_error)
//        errorLayout.visibility = View.GONE
//
//        // RecyclerView
//        val recyclerView = findViewById<RecyclerView>(R.id.search_activity_recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(
//            this,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        // Адаптер
//        musicAdapter = MusicAdapter(this)
//        recyclerView.adapter = musicAdapter
//
//        Creator.updateSearchUseCase(vm)
//        vm.initList() // Создает пустой список, если в нем null
//        vm.newList.observe(this) { list ->
//            musicAdapter.updateList(list)
//        }
//
//        // Методы активити
//        fun showNoSuchResult() {    // Сообщение при 0 найденных результатов
//            errorLayout.visibility = View.VISIBLE
//            loadingImage.visibility = View.GONE
//            errorText.text = "Ничего не найдено :("
//        }
//
//        fun showSomeSearchProblem() {   // Сообщение о любой ошибке
//            errorLayout.visibility = View.VISIBLE
//            loadingImage.visibility = View.GONE
//            errorText.text = "Непредвиденная ошибка"
//        }
//
//        fun showLoading() {     // Иконка загрузки
//            loadingImage.visibility = View.VISIBLE
//            errorLayout.visibility = View.GONE
//        }
//
//        fun hideKeyboard() {    // При работе с сервером убирает клавиатуру
//            val view: View? = this.currentFocus
//            val inputMethodManager = getSystemService(InputMethodManager::class.java)
//            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
//        }
//
//        // запрос SearchView
//        searchQueryRunnable = Runnable {
//
//            if (currentQueryText == null)
//                currentQueryText = ""
//
//            if (currentQueryText!!.isEmpty().not()) {
//
//                showLoading()   // У меня он показывается на долю секунды, даже не заметен
//
//                // Оповещаем посредника о клике
//                vm.onGetTrackListClicked(currentQueryText!!, SearchActivity.ENTITY)
//
//                loadingImage.visibility = View.GONE
//                if (currentQueryText?.isEmpty()!!.not()) {
//                    hideKeyboard()
//                }
//            }
//        }
//
//        findViewById<SearchView>(R.id.search_activity_search_view).setOnQueryTextListener( // требует listener
//
//            object : SearchView.OnQueryTextListener {               // тот самый требуемый listener
//
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    uiHandler.apply {
//                        removeCallbacks(searchQueryRunnable)
//                        post(searchQueryRunnable)
//                    }
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    uiHandler.removeCallbacks(searchQueryRunnable)
//                    currentQueryText = newText
//                    uiHandler.postDelayed(searchQueryRunnable, SearchActivity.TIMER)
//                    return true
//                }
//            }
//        )
//
//        findViewById<ImageView>(R.id.player_activity_btn_go_back).setOnClickListener {
//            finish()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false)
//    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val TRACK_ID = "track id key"
        const val ENTITY = "musicTrack"   // для поиска только музыкальных треков
        const val TIMER = 2000L
    }
}