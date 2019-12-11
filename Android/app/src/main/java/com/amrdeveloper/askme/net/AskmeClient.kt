package com.amrdeveloper.askme.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object AskmeClient {

    private val retrofit: Retrofit
    private const val API_VERSION = "v1"
    private const val API_SERVER_HOST = "http://192.168.1.8"
    private const val API_SERVER_PORT = 3000
    private const val SERVER_API_URL = "${API_SERVER_HOST}:${API_SERVER_PORT}/${API_VERSION}/"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }
}