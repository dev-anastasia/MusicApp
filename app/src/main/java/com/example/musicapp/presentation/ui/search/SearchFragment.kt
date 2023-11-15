package com.example.musicapp.presentation.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.SearchViewModel
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.search.searchAdapter.TracksAdapter

class SearchFragment : Fragment(R.layout.fragment_search),
    OnTrackClickListener {

    private lateinit var searchQueryRunnable: Runnable
    private lateinit var tracksAdapter: TracksAdapter
    private val vm: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uiHandler = Handler(Looper.getMainLooper())
        var currentQueryText: String? = ""  // текущий текст запроса

        val errorText: TextView = view.findViewById(R.id.search_fragment_tv_error)
        val loadingImage: ImageView = view.findViewById(R.id.search_fragment_iv_loading)
        val errorLayout: FrameLayout = view.findViewById(R.id.search_fragment_fl_error)
        val searchView: SearchView = view.findViewById(R.id.search_fragment_search_view)
        val goBackBtn: ImageView = view.findViewById(R.id.search_fragment_btn_go_back)

        val recyclerView = view.findViewById<RecyclerView>(R.id.search_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        tracksAdapter = TracksAdapter(this)
        recyclerView.adapter = tracksAdapter

        Creator.setSearchUseCaseVM(vm)
        vm.newList.observe(viewLifecycleOwner) { list ->
            tracksAdapter.updateList(list)
        }

        // Методы активити
        fun showNoSuchResult() {    // Сообщение при 0 найденных результатов
            errorLayout.visibility = View.VISIBLE
            loadingImage.visibility = View.GONE
            errorText.text = "Ничего не найдено :("
        }

        fun showSomeSearchProblem() {   // Сообщение о любой ошибке
            errorLayout.visibility = View.VISIBLE
            loadingImage.visibility = View.GONE
            errorText.text = "Непредвиденная ошибка"
        }

        fun showLoading() {     // Иконка загрузки
            loadingImage.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
        }

        fun hideKeyboard() {    // При работе с сервером убирает клавиатуру
            val v: View? = activity?.currentFocus
            val inputMethodManager = activity?.getSystemService(InputMethodManager::class.java)
            inputMethodManager?.hideSoftInputFromWindow(
                v?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        // запрос SearchView
        searchQueryRunnable = Runnable {
            if (currentQueryText == null)
                currentQueryText = ""

            if (currentQueryText!!.isEmpty().not()) {

                showLoading()   // У меня он показывается на долю секунды, даже не заметен

                // Оповещаем посредника о клике
                vm.onGetTrackListClicked(currentQueryText!!, ENTITY)

                loadingImage.visibility = View.GONE
                if (currentQueryText?.isEmpty()!!.not())
                    hideKeyboard()

            }
        }

        searchView.setOnQueryTextListener( // требует listener

            object : SearchView.OnQueryTextListener {     // тот самый требуемый listener

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

        goBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onItemClick(id: Long) {

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        playerFragment.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.search_container, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val TRACK_ID = "track id key"
        const val ENTITY = "musicTrack"   // для поиска только музыкальных треков
        const val TIMER = 2000L
    }
}