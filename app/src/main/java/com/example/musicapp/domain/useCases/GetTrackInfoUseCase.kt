package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.TrackInfoRepo

class GetTrackInfoUseCase(
    private val repos: TrackInfoRepo,
    private var vm: TrackInfoListener? = null
) {

    private fun queryResponseTrackInfo(hashmap: HashMap<String, String>) {
        vm?.updateLD(hashmap)
    }

    fun getTrackInfo(currentId: Long) {
        val hashmap = repos.getTrackInfo(currentId)
        queryResponseTrackInfo(hashmap)
    }

    fun setVM(vm: TrackInfoListener) {
        this.vm = vm
    }
}