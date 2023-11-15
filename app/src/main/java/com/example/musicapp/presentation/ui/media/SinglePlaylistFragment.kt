package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.ui.search.searchAdapter.TracksAdapter

class SinglePlaylistFragment : Fragment(R.layout.single_playlist_fragment),
    OnTrackClickListener {

    private lateinit var tracksAdapter: TracksAdapter
    private val vm: TracksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Адаптер - используется тот же, что в поисковике
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.single_playlist_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        tracksAdapter = TracksAdapter(this)
        recyclerView.adapter = tracksAdapter

        val id = this@SinglePlaylistFragment.arguments?.getInt(ID_KEY)

        vm.allTracks.observe(viewLifecycleOwner) { list ->
            // Обновить список адаптера
        }

        if (vm.allTracks.value!!.isEmpty()) {
            view.findViewById<LinearLayout>(R.id.ll_single_playlist_fragment_empty_playlist)
                .visibility = View.VISIBLE
        }

        view.findViewById<ImageButton>(R.id.single_playlist_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }
    }

    override fun onItemClick(id: Long) {
        // Открытие PlayerFragment'а
        TODO("Not yet implemented")
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}