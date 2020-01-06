package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.Notification
import retrofit2.Response
import retrofit2.http.*

interface NotificationService {

    @GET("notifications/")
    suspend fun getUserNotifications(
        @Header("authorization") token: String,
        @Query("id") userId: String,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): List<Notification>

    @PUT("notifications/open/{id}")
    suspend fun makeNotificationOpened(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Response<String>
}