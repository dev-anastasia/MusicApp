package com.example.musicapp.network

import android.os.Handler
import android.os.Looper

class WorkerThread : Thread() {

    lateinit var handler: Handler

    override fun run() {
        val mThread = Thread("My Background Thread")
        mThread.start()
        Looper.prepare()
        handler = Handler(Looper.myLooper()!!)
        Looper.loop()
    }
}