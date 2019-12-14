package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.Notification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NotificationService {

    @GET("notifications/")
    fun getUserNotifications(
        @Header("authorization") token : String,
        @Query("id") userId: String,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): Call<List<Notification>>
}