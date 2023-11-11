package com.example.musicapp.presentation.ui.media.tracksAdapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.entities.MusicPiece
import com.example.musicapp.data.database.FavTrackEntity
import com.example.musicapp.data.database.TrackEntity

class TrackEntityDiffUtilCallback(
    private val oldList: List<FavTrackEntity>,
    private val newList: List<FavTrackEntity>) : DiffUtil.Callback() {

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
                oldList[oldItemPosition].artworkUrl60 == newList[newItemPosition].artworkUrl60)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val bundle = Bundle()
        if (oldList[oldItemPosition].artistName != newList[newItemPosition].artistName)
            bundle.putString(NAME, newList[newItemPosition].artistName)
        if (oldList[oldItemPosition].trackName != newList[newItemPosition].trackName)
            bundle.putString(TRACK, newList[newItemPosition].trackName)
        if (oldList[oldItemPosition].artworkUrl60 != newList[newItemPosition].artworkUrl60)
            bundle.putString(COVER, newList[newItemPosition].artworkUrl60)

        return bundle
    }

    companion object {
        const val NAME = "artist name"
        const val TRACK = "track name"
        const val COVER = "song cover"
    }
}