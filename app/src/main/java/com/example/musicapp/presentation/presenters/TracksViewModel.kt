package com.example.musicapp.presentation.presenters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.data.database.FavTrackEntity

class TracksViewModel : ViewModel() {

    val allTracks = MutableLiveData<List<FavTrackEntity>>()
    private val useCase = Creator.favTracksRepoUseCase

    init {
        allTracks.value = emptyList()
    }

    fun getFavTracksList(context: Context) {
        var list = emptyList<FavTrackEntity>()
        val thread = Thread {
            list = useCase.getAllFavTracks(context)
        }
        thread.apply {
            start()
            join()
        }
        updateList(list)
    }

    private fun updateList(list: List<FavTrackEntity>) {
        this.allTracks.value = list
    }
}