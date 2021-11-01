package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.Notification
import retrofit2.Response

interface NotificationDataSource {

    suspend fun getUserNotifications(token : String, userId : String, page : Int) : Result<List<Notification>>

    suspend fun makeNotificationOpened(token : String, id : String) : Result<Response<String>>
}