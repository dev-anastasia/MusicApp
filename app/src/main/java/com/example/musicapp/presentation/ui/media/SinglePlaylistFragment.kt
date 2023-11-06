package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.PlaylistViewModel
import com.example.musicapp.presentation.ui.search.adapter.TracksAdapter

class SinglePlaylistFragment : Fragment(R.layout.single_playlist_fragment),
    OnTrackClickListener {

    private lateinit var tracksAdapter: TracksAdapter
    private val vm: PlaylistViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Адаптер - используется тот же, что в поисковике
        val recyclerView = view.findViewById<RecyclerView>(R.id.media_fragment_recycler_view)
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
    }

    override fun onItemClick(id: Long) {
        // Открытие PlayerFragment'а
        TODO("Not yet implemented")
    }
}