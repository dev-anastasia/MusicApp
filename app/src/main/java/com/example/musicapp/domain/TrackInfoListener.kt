package com.example.musicapp.domain

interface TrackInfoListener {

    fun updateLiveData(hashmap: HashMap<String, String>)
}