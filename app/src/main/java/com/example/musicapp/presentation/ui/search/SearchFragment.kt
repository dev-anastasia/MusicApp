package com.example.musicapp.presentation.ui.search

import android.content.Context
import android.os.Bundle
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
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.SearchViewModel
import com.example.musicapp.presentation.presenters.factories.SearchVMFactory
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
    private var searchQueryRunnable: Runnable? = null
    private var queryCoroutine: Job? = null
    private var currentQueryText: String = ""  // текущий текст запроса

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val searchSubcomponent =
            requireActivity().applicationContext.component.searchSubcomponent().create()
        searchSubcomponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler View
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        val trackAdapter = TrackAdapter(this)
        recyclerView.adapter = trackAdapter

        // Запрос в Retrofit
        searchQueryRunnable = Runnable {
            if (currentQueryText.isNullOrEmpty().not()) {
                vm.onGetTracksListClicked(currentQueryText)
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
                        hideLoadingIcon()
                        showSomeSearchProblem()
                    }

                    SearchUIState.Success -> {
                        hideMessageLayout()
                        hideLoadingIcon()
                        recyclerView.visibility = View.VISIBLE
                    }

                    SearchUIState.NoResults -> {
                        hideLoadingIcon()
                        showNoSuchResult()
                    }
                }
            }
        }
    }

    override fun onResume() {

        requireView().findViewById<SearchView>(R.id.search_fragment_search_view)
            .setOnQueryTextListener(      // требует listener (SearchView.OnQueryTextListener)
                object : SearchView.OnQueryTextListener {     // тот самый требуемый listener

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        queryCoroutine?.cancel()
                        CoroutineScope(Dispatchers.Main).launch {
                            searchQueryRunnable?.run()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        currentQueryText = newText!!
                        queryCoroutine?.cancel()
                        queryCoroutine = CoroutineScope(Dispatchers.Main).launch {
                            delay(DELAY_TIMER)
                            searchQueryRunnable?.run()
                        }
                        return true
                    }
                }
            )

        requireView().findViewById<ImageView>(R.id.search_fragment_btn_go_back).setOnClickListener {
            onBackPressed()
        }

        super.onResume()
    }

    override fun onDestroy() {
        searchQueryRunnable = null
        queryCoroutine?.cancel()
        queryCoroutine = null
        super.onDestroy()
    }

    override fun onItemClick(id: Long) {

        val playerFragment = PlayerFragment()
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
        requireView().findViewById<ImageView>(R.id.search_fragment_iv_loading)
            .visibility = View.VISIBLE
    }

    private fun showSomeSearchProblem() {
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
        const val DELAY_TIMER = 2000L
    }
}