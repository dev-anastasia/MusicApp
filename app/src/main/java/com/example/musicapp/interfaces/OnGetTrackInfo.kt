package com.example.musicapp.interfaces

interface OnGetTrackInfo {

    fun getTrackInfo(currentId: Long) : HashMap<String, String>
}