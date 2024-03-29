package com.example.musicapp.presentation.ui.media

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.presenters.factories.TracksVMFactory
import com.example.musicapp.presentation.ui.media.viewpager.TracksListUiState
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.trackAdapter.TrackAdapter
import javax.inject.Inject

class SinglePlaylistFragment : Fragment(R.layout.single_playlist_fragment),
    OnTrackClickListener {

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
            view.findViewById(R.id.ll_single_playlist_fragment_empty_playlist)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.single_playlist_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel (observers)
        val trackAdapter = TrackAdapter(this)
        recyclerView.adapter = trackAdapter

        vm.apply {

            getTracksList(requireArguments().getInt(ID_KEY))     // Получаем список треков

            tracksList.observe(viewLifecycleOwner) {
                trackAdapter.updateList(it)
            }

            uiState.observe(viewLifecycleOwner) {
                when (it) {
                    TracksListUiState.Loading -> {
                        emptyPlaylistMessage.visibility = View.GONE
                    }

                    TracksListUiState.Success -> {
                        emptyPlaylistMessage.visibility = View.GONE
                    }

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
    }

    override fun onResume() {
        requireView().findViewById<ImageButton>(R.id.single_playlist_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }

        super.onResume()
    }

    override fun onItemClick(id: Long) {
        // Открытие PlayerFragment'а
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        bundle.putInt(PLAYLIST_ID, this.requireArguments().getInt(ID_KEY))
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private companion object {
        const val ID_KEY = "id key"
        const val TRACK_ID = "track id key"
        const val PLAYLIST_ID = "playlist id key"
    }
}