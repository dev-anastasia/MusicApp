package com.example.musicapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    val isLikedLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isAddedLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val coverImageLinkLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val songNameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val artistNameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val durationLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}