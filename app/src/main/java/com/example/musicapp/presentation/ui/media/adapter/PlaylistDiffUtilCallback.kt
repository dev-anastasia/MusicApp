package com.example.musicapp.presentation.ui.media.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.entities.room.PlaylistDBObject

class PlaylistDiffUtilCallback(
    private val oldList: MutableList<PlaylistDBObject>,
    private val newList: List<PlaylistDBObject>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].cover == newList[newItemPosition].cover
                &&
                oldList[oldItemPosition].name == newList[newItemPosition].name
                &&
                oldList[oldItemPosition].songCount == newList[newItemPosition].songCount)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val bundle = Bundle()
        if (oldList[oldItemPosition].name != newList[newItemPosition].name)
            bundle.putString(NAME, newList[newItemPosition].name)
        if (oldList[oldItemPosition].cover != newList[newItemPosition].cover)
            bundle.putString(COVER, newList[newItemPosition].cover)
        if (oldList[oldItemPosition].songCount != newList[newItemPosition].songCount)
            bundle.putInt(COUNT, newList[newItemPosition].songCount)

        return bundle
    }

    companion object {
        const val NAME = "playlist name"
        const val COUNT = "tracks count"
        const val COVER = "playlist cover"
    }
}