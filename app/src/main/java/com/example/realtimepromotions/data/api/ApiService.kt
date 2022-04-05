package com.example.realtimepromotions.data.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET
    fun main(): Call<String>
}