package com.example.musicapp.presentation.ui.media.viewpager

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.trackAdapter.TrackAdapter

class FavsFragment : Fragment(R.layout.favs_fragment), OnTrackClickListener {

    private lateinit var trackAdapter: TrackAdapter
    private val vm: TracksViewModel by viewModels()
    private val emptyPlaylistMessage: LinearLayout
        get() {
            return requireView().findViewById(R.id.ll_favs_playlist_fragment_empty_playlist)
        }
    private val apContext: Context
        get() {
            return requireActivity().applicationContext
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.favs_fragment_recycler_view)
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

            if (it.isEmpty())
                emptyPlaylistMessage.visibility = View.VISIBLE
            else
                emptyPlaylistMessage.visibility = View.GONE
        }
    }

    override fun onResume() {

        vm.getTracksList(apContext, -1) // Получаем список треков

        super.onResume()
    }

    override fun onItemClick(id: Long) {
        // Открытие фрагмента со списком треков плейлиста, id передаётся адаптером
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        playerFragment.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.media_container_main, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    companion object {
        const val TRACK_ID = "track id key"
    }
}