package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.trackAdapter.TrackAdapter

class FavsFragment : Fragment(R.layout.favs_fragment), OnTrackClickListener {

    private lateinit var trackAdapter: TrackAdapter
    private val vm: TracksViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyPlaylistMessage: LinearLayout =
            view.findViewById(R.id.ll_favs_playlist_fragment_empty_playlist)

        recyclerView = view.findViewById(R.id.favs_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        trackAdapter = TrackAdapter(this)
        recyclerView.adapter = trackAdapter

        vm.tracksList.observe(viewLifecycleOwner) {
            trackAdapter.updateList(it)
        }

        vm.uiState.observe(viewLifecycleOwner) {
            when (it) {
                TracksListUiState.Loading -> {}

                TracksListUiState.Success -> {}

                TracksListUiState.NoResults -> {
                    emptyPlaylistMessage.visibility = View.VISIBLE
                }

                else -> {
                    Log.e("uiState", "SinglePlaylistFragment: some uiState error")
                    Toast.makeText(
                        context,
                        "Непредвиденная ошибка: не удалось получить список треков",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        vm.getTracksList(Creator.dao.favsPlaylistId()) // Получаем список треков
        super.onResume()
    }

    override fun onDestroy() {
        recyclerView.adapter = null
        super.onDestroy()
    }

    override fun onItemClick(id: Long) {
        // Открытие фрагмента со списком треков плейлиста, id передаётся адаптером
        val playerFragment = PlayerFragment(Creator.playerClass)
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        bundle.putInt(PLAYLIST_ID, Creator.dao.favsPlaylistId())
        playerFragment.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.main_container, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    private companion object {
        const val TRACK_ID = "track id key"
        const val PLAYLIST_ID = "playlist id key"
    }
}