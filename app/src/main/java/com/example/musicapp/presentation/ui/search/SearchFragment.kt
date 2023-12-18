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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.factories.SearchVMFactory
import com.example.musicapp.presentation.presenters.SearchViewModel
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.trackAdapter.TrackAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.fragment_search), OnTrackClickListener {

    @Inject
    lateinit var vmFactory: SearchVMFactory
    private lateinit var vm: SearchViewModel
    private var currentQueryText: String? = ""  // текущий текст запроса
    private lateinit var searchQueryRunnable: Runnable
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var uiHandler: Handler
    private lateinit var recyclerView: RecyclerView
    private var coroutineChangeText: Job? = null

    // ПЕРЕОПРЕДЕЛЁННЫЕ МЕТОДЫ + МЕТОДЫ ЖЦ:

    override fun onCreate(savedInstanceState: Bundle?) {

        requireActivity().applicationContext.component.inject(this)
        vm = ViewModelProvider(this, vmFactory)[SearchViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiHandler = Handler(Looper.getMainLooper())

        recyclerView = view.findViewById(R.id.search_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        trackAdapter = TrackAdapter(this)
        recyclerView.adapter = trackAdapter

        // Инициализируем lateinit vars
        searchQueryRunnable = Runnable {
            if (currentQueryText.isNullOrEmpty().not()) {
                vm.onGetTracksListClicked(currentQueryText!!)
            }
        }

        // Устанавливаем observers:
        vm.apply {

            searchResultsList.observe(viewLifecycleOwner) { list ->
                trackAdapter.updateList(list)
            }

            searchUiState.observe(viewLifecycleOwner) {
                when (it) {
                    SearchUIState.Loading -> {
                        hideMessageLayout()
                        hideKeyboard()
                        showLoadingIcon()
                        recyclerView.visibility = View.GONE
                    }

                    SearchUIState.Error -> {
                        showSomeSearchProblem()
                        recyclerView.visibility = View.GONE
                    }

                    SearchUIState.Success -> {
                        hideKeyboard()
                        hideLoadingIcon()
                        recyclerView.visibility = View.VISIBLE
                    }

                    SearchUIState.NoResults -> {
                        showNoSuchResult()
                        recyclerView.visibility = View.GONE
                    }

                    else -> {
                        throw IllegalStateException("Wrong uiState!")
                    }
                }
            }
        }
    }

    override fun onResume() {

        requireView().findViewById<SearchView>(R.id.search_fragment_search_view)
            .setOnQueryTextListener( // требует listener
                object : SearchView.OnQueryTextListener {     // тот самый требуемый listener

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        coroutineChangeText?.cancel()
                        CoroutineScope(Dispatchers.Main).launch() {
                            searchQueryRunnable.run()
                        }
                        return true
                    }


                    override fun onQueryTextChange(newText: String?): Boolean {
                        currentQueryText = newText
                        coroutineChangeText?.cancel()
                        coroutineChangeText = CoroutineScope(Dispatchers.Main).launch {
                            delay(TIMER)
                            searchQueryRunnable.run()
                        }
                        return true
                    }
                }
            )

        requireView().findViewById<ImageView>(R.id.search_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }

        super.onResume()
    }

    override fun onDestroy() {
        recyclerView.adapter = null
        super.onDestroy()
    }

    override fun onItemClick(id: Long) {

        val playerFragment = PlayerFragment(Creator.playerClass)
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    // ПРИВАТНЫЕ МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ UI:

    private fun showLoadingIcon() {     // Иконка загрузки
        hideMessageLayout()
        requireView().findViewById<ImageView>(R.id.search_fragment_iv_loading)
            .visibility = View.VISIBLE
    }

    private fun showSomeSearchProblem() {
        hideLoadingIcon()
        requireView().findViewById<FrameLayout>(R.id.search_fragment_fl_error)
            .visibility = View.VISIBLE
        requireView().findViewById<TextView>(R.id.search_fragment_tv_error)
            .text = R.string.unexpected_error_message.toString()
    }

    private fun showNoSuchResult() {    // Сообщение при 0 найденных результатов
        hideLoadingIcon()
        requireView().findViewById<FrameLayout>(R.id.search_fragment_fl_error)
            .visibility = View.VISIBLE
        requireView().findViewById<TextView>(R.id.search_fragment_tv_error)
            .text = R.string.nothing_found_message.toString()
    }

    private fun hideMessageLayout() {
        requireView().findViewById<FrameLayout>(R.id.search_fragment_fl_error)
            .visibility = View.GONE
    }

    private fun hideLoadingIcon() {
        requireView().findViewById<ImageView>(R.id.search_fragment_iv_loading)
            .visibility = View.GONE
    }

    private fun hideKeyboard() {    // При работе с сервером убирает клавиатуру
        val v: View? = requireActivity().currentFocus
        val inputMethodManager = requireActivity().getSystemService(InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(
            v?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private companion object {
        const val TRACK_ID = "track id key"
        const val TIMER = 2000L
    }
}