package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.Notification
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {

    @GET("notifications/")
    fun getUserNotifications(
        @Query("id") userId: String,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): List<Notification>
}