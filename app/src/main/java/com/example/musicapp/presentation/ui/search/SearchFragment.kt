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
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.SearchViewModel
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.player.UIState
import com.example.musicapp.presentation.ui.search.searchAdapter.TracksAdapter

class SearchFragment : Fragment(R.layout.fragment_search),
    OnTrackClickListener {

    private val vm: SearchViewModel by viewModels()

    private lateinit var searchQueryRunnable: Runnable
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var uiHandler: Handler
    private var currentQueryText: String? = ""  // текущий текст запроса

    private val errorText: TextView
        get() {
            return requireView().findViewById(R.id.search_fragment_tv_error)
        }
    private val loadingImage: ImageView
        get() {
            return requireView().findViewById(R.id.search_fragment_iv_loading)
        }
    private val errorLayout: FrameLayout
        get() {
            return requireView().findViewById(R.id.search_fragment_fl_error)
        }
    private val searchView: SearchView
        get() {
            return requireView().findViewById(R.id.search_fragment_search_view)
        }
    private val goBackBtn: ImageView
        get() {
            return requireView().findViewById(R.id.search_fragment_btn_go_back)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiHandler = Handler(Looper.getMainLooper())

        val recyclerView = view.findViewById<RecyclerView>(R.id.search_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        tracksAdapter = TracksAdapter(this)
        recyclerView.adapter = tracksAdapter

        vm.newList.observe(viewLifecycleOwner) { list ->
            tracksAdapter.updateList(list)
        }


        vm.uiState.observe(viewLifecycleOwner) {
            when (it) {
                (UIState.Loading) -> {
                    hideNoSuchResults()
                    hideKeyboard()
                    showLoadingIcon()
                }

                (UIState.Error) -> {
                    showNoSuchResult()
                }

                (UIState.Success) -> {
                    hideLoadingIcon()
                }

                else -> throw IllegalStateException("Wrong uiState!")
            }
        }

        // запрос SearchView
        searchQueryRunnable = Runnable {
            if (currentQueryText == null)
                currentQueryText = ""

            if (currentQueryText!!.isEmpty().not()) {

                vm.onGetTracksListClicked(currentQueryText!!, ENTITY)
            }
        }
    }

    // Методы активити
    private fun showLoadingIcon() {     // Иконка загрузки
        if (tracksAdapter.getList().isEmpty().not())
            tracksAdapter.updateList(emptyList())
        loadingImage.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    private fun hideLoadingIcon() {
        loadingImage.visibility = View.GONE
    }

    private fun showNoSuchResult() {    // Сообщение при 0 найденных результатов
        if (tracksAdapter.getList().isEmpty().not())
            tracksAdapter.updateList(emptyList())
        errorLayout.visibility = View.VISIBLE
        loadingImage.visibility = View.GONE
        errorText.text = "Ничего не найдено :("
    }

    private fun hideNoSuchResults() {
        errorLayout.visibility = View.GONE
    }

    private fun hideKeyboard() {    // При работе с сервером убирает клавиатуру
        val v: View? = activity?.currentFocus
        val inputMethodManager = activity?.getSystemService(InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(
            v?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onResume() {

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

        super.onResume()
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