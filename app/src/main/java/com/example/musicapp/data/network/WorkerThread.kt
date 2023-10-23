package com.example.musicapp.data.network

import android.os.Handler
import android.os.Looper

class WorkerThread : Thread() {

    var mHandler: Handler? = null

    override fun run() {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        mHandler = Handler(Looper.myLooper()!!)
        Looper.loop()
    }
}
