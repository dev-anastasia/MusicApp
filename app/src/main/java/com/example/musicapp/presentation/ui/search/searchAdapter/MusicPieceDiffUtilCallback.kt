package com.example.musicapp.presentation.ui.search.searchAdapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.entities.MusicTrack

class MusicPieceDiffUtilCallback(
    private val oldList: List<MusicTrack>,
    private val newList: List<MusicTrack>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].trackId == newList[newItemPosition].trackId)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].artistName == newList[newItemPosition].artistName
                &&
                oldList[oldItemPosition].trackName == newList[newItemPosition].trackName
                &&
                oldList[oldItemPosition].artworkUrl100 == newList[newItemPosition].artworkUrl100)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val bundle = Bundle()
        if (oldList[oldItemPosition].artistName != newList[newItemPosition].artistName)
            bundle.putString(NAME, newList[newItemPosition].artistName)
        if (oldList[oldItemPosition].trackName != newList[newItemPosition].trackName)
            bundle.putString(TRACK, newList[newItemPosition].trackName)
        if (oldList[oldItemPosition].artworkUrl100 != newList[newItemPosition].artworkUrl100)
            bundle.putString(COVER, newList[newItemPosition].artworkUrl100)

        return bundle
    }

    companion object {
        const val NAME = "artist name"
        const val TRACK = "track name"
        const val COVER = "song cover"
    }
}