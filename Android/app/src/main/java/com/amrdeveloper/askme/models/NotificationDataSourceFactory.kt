package com.amrdeveloper.askme.models

import androidx.lifecycle.MutableLiveData
import com.amrdeveloper.askme.data.Notification
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.models.NotificationDataSource

class NotificationDataSourceFactory(var userId : String, val token : String) : DataSource.Factory<Int, Notification>() {

    private val notificationLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Notification>> = MutableLiveData()

    override fun create(): DataSource<Int, Notification> {
        val notifcationDataSource =
            NotificationDataSource(userId,token)
        notificationLiveDataSource.postValue(notifcationDataSource)
        return notifcationDataSource
    }

    fun getNotificationLiveDataSource() = notificationLiveDataSource
}