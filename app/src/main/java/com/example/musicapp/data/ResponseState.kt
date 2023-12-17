package com.example.musicapp.data

sealed class ResponseState<out Int> {   // Пока что не используется

    object Success : ResponseState<Int>()

    object Error : ResponseState<Int>()
}