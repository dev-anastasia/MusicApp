package com.example.musicapp

import android.inputmethodservice.InputMethodService

fun hideKeyboard() {
    InputMethodService().onFinishInput()
}