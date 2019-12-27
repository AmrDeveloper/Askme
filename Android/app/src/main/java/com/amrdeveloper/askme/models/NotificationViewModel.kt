package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Notification

class NotificationViewModel : ViewModel() {

    private var notificationPagedList: LiveData<PagedList<Notification>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Notification>>

    fun loadUserNotifications(id : String, token : String){
        val dataSourceFactory = NotificationDataSourceFactory(id, token)
        liveDataSource = dataSourceFactory.getNotificationLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        notificationPagedList = LivePagedListBuilder(dataSourceFactory, config).build()
    }

    fun getNotificationList() = notificationPagedList
}