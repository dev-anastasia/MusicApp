package com.example.musicapp.presentation.presenters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.database.TrackEntity

class TracksViewModel : ViewModel() {

    val allTracks = MutableLiveData<List<TrackEntity>>()
    private val useCase = Creator.favTracksRepoUseCase

    init {
        allTracks.value = emptyList()
    }

    fun getTracksIdsList(context: Context, playlistId: Int) {
        Thread {
            useCase.getTracksIds(context, playlistId) { idsList -> // Здесь - список id треков
                if (idsList.isEmpty())
                    println("Empty list")
                else
                    useCase.getTracksList(context, idsList) { tracksList -> // Здесь - список треков
                        updateList(tracksList)
                    }
            }
        }.start()
    }

    private fun updateList(list: List<TrackEntity>) {
        this.allTracks.value = list
    }
}