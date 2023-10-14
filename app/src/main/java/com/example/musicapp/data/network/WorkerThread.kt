package com.example.musicapp.data.network

import android.os.Handler
import android.os.Looper

class WorkerThread : Thread() {

    var mHandler: Handler = Handler(Looper.myLooper()!!)

    override fun run() {
        Looper.prepare()
        Looper.loop()
    }
}