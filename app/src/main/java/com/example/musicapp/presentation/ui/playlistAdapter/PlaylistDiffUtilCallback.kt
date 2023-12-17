package com.example.musicapp.presentation.ui.playlistAdapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.entities.Playlist

class PlaylistDiffUtilCallback(

    private val oldList: MutableList<Playlist>,
    private val newList: List<Playlist>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].playlistId == newList[newItemPosition].playlistId)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].playlistCover == newList[newItemPosition].playlistCover
                &&
                oldList[oldItemPosition].playlistName == newList[newItemPosition].playlistName)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val bundle = Bundle()
        if (oldList[oldItemPosition].playlistName != newList[newItemPosition].playlistName) {
            bundle.putString(NAME, newList[newItemPosition].playlistName)
        }
        if (oldList[oldItemPosition].playlistCover != newList[newItemPosition].playlistCover) {
            bundle.putString(COVER, newList[newItemPosition].playlistCover)
        }
        if (oldList[oldItemPosition].systemTimeMillis != newList[newItemPosition].systemTimeMillis) {
            bundle.putLong(TIME, newList[newItemPosition].systemTimeMillis)
        }
        return bundle
    }

    companion object {
        const val NAME = "playlist name"
        const val COUNT = "tracks count"
        const val COVER = "playlist cover"
        const val TIME = "time millis"
    }
}