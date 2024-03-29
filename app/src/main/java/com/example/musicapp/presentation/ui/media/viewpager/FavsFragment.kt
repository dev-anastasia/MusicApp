package com.example.musicapp.presentation.ui.media.viewpager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.MyObject.FAVS_PLAYLIST_ID
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.presenters.factories.TracksVMFactory
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.trackAdapter.TrackAdapter
import javax.inject.Inject

class FavsFragment : Fragment(R.layout.favs_fragment), OnTrackClickListener {

    @Inject
    lateinit var vmFactory: TracksVMFactory
    private lateinit var vm: TracksViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mediaSubcomponent =
            requireActivity().applicationContext.component.mediaSubcomponent().create()
        mediaSubcomponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[TracksViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyPlaylistMessage: LinearLayout =
            view.findViewById(R.id.ll_favs_playlist_fragment_empty_playlist)
        val loadingIcon: ImageView = view.findViewById(R.id.favs_fragment_iv_loading)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favs_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        val trackAdapter = TrackAdapter(this)
        recyclerView.adapter = trackAdapter

        vm.apply {

            getTracksList(FAVS_PLAYLIST_ID) // Получаем список треков

            tracksList.observe(viewLifecycleOwner) {
                trackAdapter.updateList(it)
            }

            uiState.observe(viewLifecycleOwner) {
                when (it) {
                    TracksListUiState.Loading -> {
                        loadingIcon.visibility = View.VISIBLE
                    }

                    TracksListUiState.Success -> {
                        loadingIcon.visibility = View.GONE
                    }

                    TracksListUiState.NoResults -> {
                        loadingIcon.visibility = View.GONE
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
    }

    override fun onItemClick(id: Long) {
        // Открытие фрагмента со списком треков плейлиста, id передаётся адаптером
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        bundle.putInt(PLAYLIST_ID, FAVS_PLAYLIST_ID)
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
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