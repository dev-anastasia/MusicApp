package com.example.musicapp.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.SearchFragment
import com.example.musicapp.presentation.presenters.SearchViewModel

class SearchActivity : AppCompatActivity() {

    //    private lateinit var searchQueryRunnable: Runnable
//    private lateinit var musicAdapter: MusicAdapter
    private val vm: SearchViewModel by viewModels()

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_fragment_container_view, SearchFragment())
            .addToBackStack("SearchFragment")
            .commit()
//
//        val uiHandler = Handler(Looper.getMainLooper())
//        var currentQueryText: String? = ""  // текущий текст запроса
//
//        val errorText: TextView = findViewById(R.id.search_activity_tv_error)
//        // Иконка загрузки
//        val loadingImage: ImageView = findViewById(R.id.search_activity_iv_loading)
//        loadingImage.visibility = GONE
//        // Картинка + сообщение о проблеме:
//        val errorLayout: FrameLayout = findViewById(R.id.search_activity_fl_error)
//        errorLayout.visibility = GONE
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
//            errorLayout.visibility = VISIBLE
//            loadingImage.visibility = GONE
//            errorText.text = "Ничего не найдено :("
//        }
//
//        fun showSomeSearchProblem() {   // Сообщение о любой ошибке
//            errorLayout.visibility = VISIBLE
//            loadingImage.visibility = GONE
//            errorText.text = "Непредвиденная ошибка"
//        }
//
//        fun showLoading() {     // Иконка загрузки
//            loadingImage.visibility = VISIBLE
//            errorLayout.visibility = GONE
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
//                vm.onGetTrackListClicked(currentQueryText!!, ENTITY)
//
//                loadingImage.visibility = GONE
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
//                    uiHandler.postDelayed(searchQueryRunnable, TIMER)
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
//    override fun onItemClick(id: Long) {
//        val intent = Intent(this, PlayerActivity::class.java)
//        intent.putExtra(TRACK_ID, id)
//        this.startActivity(intent)
//    }

    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val TRACK_ID = "track id key"
        const val ENTITY = "musicTrack"   // для поиска только музыкальных треков
        const val TIMER = 2000L
    }

}