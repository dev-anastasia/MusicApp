package com.example.musicapp.presentation.ui.player

sealed class PlayerUIState<out Int> {

    object Loading : PlayerUIState<Nothing>()

    object Success : PlayerUIState<Nothing>() // Данные получены, трек ещё не загружен в MediaPlayer

    object DataReady : PlayerUIState<Nothing>() // Трек уже загружен в MediaPlayer

    object Error : PlayerUIState<Nothing>()
}

data class ViewState(
    val trackName: String = "",
    val artistName: String = "",
    val audioPreview: String = "",
    val durationString: String = "",
    val currentTimeString : String = "",
    val artworkUrl100: String = "",  // Больший размер (для плеера)
    val artworkUrl60: String = "",  // Меньший размер (для БД)
    val isLiked: Boolean = false
)