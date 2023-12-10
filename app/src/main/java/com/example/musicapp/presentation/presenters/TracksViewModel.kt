package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack

class TracksViewModel : ViewModel() {

    val tracksList = MutableLiveData<List<MusicTrack>>(emptyList())
    private val mainHandler = Handler(Looper.getMainLooper())

    fun getTracksList(playlistId: Int) {
        Creator.getTracksListUseCase.getPlaylistTracksList(playlistId) {
            mainHandler.post {
                updateList(it)
            }
        }
    }

    private fun updateList(list: List<MusicTrack>) {
        tracksList.postValue(list)
    }
}