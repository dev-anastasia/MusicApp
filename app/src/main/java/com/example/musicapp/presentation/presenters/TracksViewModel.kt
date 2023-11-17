package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.database.TrackEntity

class TracksViewModel : ViewModel() {

    val tracksList = MutableLiveData<List<TrackEntity>>()
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        tracksList.value = emptyList()
    }

    fun getTracksList(context: Context, playlistId: Int) {
        Thread {
            Creator.getTracksListUseCase.getTracksList(context, playlistId) {
                mainHandler.post {
                    if (it.isNotEmpty())
                        updateList(it)
                }
            }
        }.start()
    }

    private fun updateList(list: List<TrackEntity>) {
        this.tracksList.value = list
    }
}