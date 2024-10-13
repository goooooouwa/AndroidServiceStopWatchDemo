package com.example.servicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(applicationContext, MyBackgroundService::class.java)
        registerReceiver(updateTime, IntentFilter(MyBackgroundService.UPDATED_TIME))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStart.setOnClickListener {
            startOrStop()
        }

        binding.btnReset.setOnClickListener {
            reset()
        }
    }

    private fun startOrStop() {
        if (isStarted) {
            stop()
        } else {
            start()
        }
    }

    private fun start() {
        Log.i(MyBackgroundService.TAG, "Starting service")
        serviceIntent.putExtra(MyBackgroundService.CURRENT_TIME, time)
        startService(serviceIntent)
        binding.btnStart.text = "Stop"
        isStarted = true
    }

    private fun stop() {
        Log.i(MyBackgroundService.TAG, "Stopping service")
        stopService(serviceIntent)
        binding.btnStart.text = "Start"
        isStarted = false
    }

    private fun reset() {
        stop()
        time = 0.0
        binding.tvTime.text = "00:00:00"
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(MyBackgroundService.CURRENT_TIME,0.0)
            binding.tvTime.text = time.toString()
        }

    }
}