package com.example.realtimepromotions.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiManager {
    lateinit var apiService: ApiService

    init {
        initService(initRetrofit())
    }

    private fun initService(retrofit: Retrofit) {
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .client(initOkhttpClient())
            .build()
    }

    fun initOkhttpClient():OkHttpClient {
        val builder =OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .callTimeout(120, TimeUnit.SECONDS)
            .hostnameVerifier{ _, _ -> true }
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .addInterceptor(AuthorizationInterceptor())

        return builder.build()
    }

    fun initWebSocket() {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onConnectListener(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
            }

            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                super.onBlockedStatusChanged(network, blocked)
            }
        })
    }
}