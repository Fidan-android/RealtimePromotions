package com.example.realtimepromotions.domain.api

interface MessageListener {
    fun onSuccess()
    fun onError()
    fun onClose()
    fun onMessage()
}