package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.entities.MusicPiece

class PlaylistViewModel : ViewModel() {

    val newList = MutableLiveData<List<MusicPiece>>()
}