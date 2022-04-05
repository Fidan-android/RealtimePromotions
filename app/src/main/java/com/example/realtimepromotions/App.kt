package com.example.realtimepromotions

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        //set listener for network
    }
}