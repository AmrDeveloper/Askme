package com.amrdeveloper.askme.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object AskmeClient {

    private val retrofit: Retrofit
    private const val ASKME_BASE_URL = "http://192.168.1.2:3000/v1/"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(ASKME_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAskmeService(): AskmeService {
        return retrofit.create(AskmeService::class.java)
    }
}