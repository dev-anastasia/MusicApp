package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.entities.Playlist

class MediaViewModel : ViewModel() {

    val newList = MutableLiveData<List<Playlist>>()
    private val testList = ArrayList<Playlist>()

    fun initList() {
        if (newList.value == null)
            newList.value = emptyList()

        testList.add(
            Playlist(
                -1,
                "",
                "Избранное",
                1
            )
        )
        testList.add(
            Playlist(
                -2,
                "",
                "Создать новый плейлист...",
                77
            )
        )
        testList.add(
            Playlist(
                3,
                "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/0c/30/5f/0c305f8e-01ba-615b-5790-4c40a32ffa7b/13UABIM59453.rgb.jpg/100x100bb.jpg",
                "My Playlist",
                318
            )
        )
        testList.add(
            Playlist(
                4,
                "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/0c/30/5f/0c305f8e-01ba-615b-5790-4c40a32ffa7b/13UABIM59453.rgb.jpg/100x100bb.jpg",
                "My Playlist",
                5
            )
        )
        testList.add(
            Playlist(
                5,
                "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/0c/30/5f/0c305f8e-01ba-615b-5790-4c40a32ffa7b/13UABIM59453.rgb.jpg/100x100bb.jpg",
                "My Playlist",
                42
            )
        )
        testList.add(
            Playlist(
                6,
                "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/0c/30/5f/0c305f8e-01ba-615b-5790-4c40a32ffa7b/13UABIM59453.rgb.jpg/100x100bb.jpg",
                "My Playlist",
                6
            )
        )
        update(testList)
    }

    private fun update(newList: List<Playlist>) {
        this.newList.value = newList
    }
}