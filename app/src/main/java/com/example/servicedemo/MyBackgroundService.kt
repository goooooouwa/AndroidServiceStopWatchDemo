package com.example.servicedemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.Timer
import java.util.TimerTask

class MyBackgroundService: Service() {
    private val timer = Timer()

    init {
        Log.i(TAG, "Service has been created")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")
        val time = intent.getDoubleExtra(CURRENT_TIME, 0.0)
        timer.scheduleAtFixedRate(StopWatchTimerTask(time),0, 1000)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.i(TAG, "Service Destorying...")
        timer.cancel()
        super.onDestroy()
    }

    companion object{
        const val TAG = "MyTag"
        const val CURRENT_TIME = "Current time"
        const val UPDATED_TIME = "Updated time"
    }

    private inner class StopWatchTimerTask(private var time:Double): TimerTask(){
        override fun run() {
            val intent = Intent(UPDATED_TIME)
            time++
            intent.putExtra(CURRENT_TIME,time)
            sendBroadcast(intent)
        }
    }
}