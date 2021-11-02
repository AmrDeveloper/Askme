package com.amrdeveloper.askme.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.source.remote.paging.NotificationPagingDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationDataSource: NotificationDataSource) {

    fun getUserNotifications(token : String, userId : String) : Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { NotificationPagingDataSource(notificationDataSource, token, userId) }).flow
    }

    suspend fun makeNotificationOpened(token : String, id : String) : Result<Response<String>> {
        return notificationDataSource.makeNotificationOpened(token, id)
    }

}