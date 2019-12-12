package com.amrdeveloper.askme.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object AskmeClient {

    private val retrofit: Retrofit

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

    fun getNotificationService(): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }
}