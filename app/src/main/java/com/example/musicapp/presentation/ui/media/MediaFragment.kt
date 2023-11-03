package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.MediaViewModel
import com.example.musicapp.presentation.ui.media.adapter.MediaPlaylistsAdapter

class MediaFragment : Fragment(R.layout.fragment_media),
    OnPlaylistClickListener {

    private lateinit var mediaAdapter: MediaPlaylistsAdapter
    private val vm: MediaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goBackBtn = view.findViewById<ImageButton>(R.id.media_fragment_btn_go_back)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.media_fragment_playlists_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        mediaAdapter = MediaPlaylistsAdapter(this)
        recyclerView.adapter = mediaAdapter

        vm.initList() // Создает пустой список, если в нем null
        vm.newList.observe(viewLifecycleOwner) { list ->
            mediaAdapter.updateList(list)
        }

        goBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onPlaylistClick(id: Int) {

        when (id) {
            (-2) -> {
                val fr = AddPlaylistFragment()
                activity?.supportFragmentManager!!.beginTransaction()
                    .add(R.id.media_container, fr)
                    .addToBackStack("AddPlaylistFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }

            else -> {
                val fr = SinglePlaylistFragment()
                val bundle = Bundle()
                bundle.putInt(ID_KEY, id)
                fr.arguments = bundle

                activity?.supportFragmentManager!!.beginTransaction()
                    .add(R.id.media_container, fr)
                    .addToBackStack("SinglePlaylistFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    companion object {
        const val ID_KEY = "id key"
    }
}