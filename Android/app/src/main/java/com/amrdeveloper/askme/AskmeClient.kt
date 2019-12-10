package com.amrdeveloper.askme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AskmeClient {

    private val retrofit: Retrofit
    private lateinit var askmeClient: AskmeClient
    private val ASKME_BASE_URL = "http://localhost:3000/v1/"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(ASKME_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAskmeService(): AskmeService {
        return retrofit.create(AskmeService::class.java)
    }
}