package com.example.musicapp.presentation.ui.player

sealed class PlayerUIState<out Int> {

    object Success : PlayerUIState<Nothing>()

    object IsPlaying : PlayerUIState<Nothing>()

    object Error : PlayerUIState<Nothing>()
}

data class ViewState(
    val trackName: String = "",
    val artistName: String = "",
    val audioPreview: String = "",
    val durationString: String = "",
    val artworkUrl100: String = "",  // Больший размер (для плеера)
    val artworkUrl60: String = "",  // Меньший размер (для БД)
    val isLiked: Boolean = false
)