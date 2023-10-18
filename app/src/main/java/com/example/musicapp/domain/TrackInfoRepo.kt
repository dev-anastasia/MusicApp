package com.example.musicapp.domain

interface TrackInfoRepo {

    fun getTrackInfo(currentId: Long) : HashMap<String, String>
}