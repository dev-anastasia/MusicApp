package com.example.musicapp.presentation.ui.media.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.entities.database.PlaylistEntity

class PlaylistDiffUtilCallback(
    private val oldList: MutableList<PlaylistEntity>,
    private val newList: List<PlaylistEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val du = DiffUtilPlaylistItemCallback()
        return du.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val du = DiffUtilPlaylistItemCallback()
        return du.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val bundle = Bundle()
        if (oldList[oldItemPosition].name != newList[newItemPosition].name)
            bundle.putString(NAME, newList[newItemPosition].name)
        if (oldList[oldItemPosition].cover != newList[newItemPosition].cover)
            bundle.putString(COVER, newList[newItemPosition].cover)
        if (oldList[oldItemPosition].songCount != newList[newItemPosition].songCount)
            bundle.putInt(COUNT, newList[newItemPosition].songCount)
        if (oldList[oldItemPosition].timeMillis != newList[newItemPosition].timeMillis)
            bundle.putLong(TIME, newList[newItemPosition].timeMillis)

        return bundle
    }

    companion object {
        const val NAME = "playlist name"
        const val COUNT = "tracks count"
        const val COVER = "playlist cover"
        const val TIME = "time millis"
    }
}