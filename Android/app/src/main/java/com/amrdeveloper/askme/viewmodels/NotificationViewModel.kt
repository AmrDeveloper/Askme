package com.amrdeveloper.askme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.models.Notification

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

    private inner class NotificationDataSourceFactory(var userId: String, val token: String) :
        DataSource.Factory<Int, Notification>() {

        private val notificationLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Notification>> =
            MutableLiveData()

        override fun create(): DataSource<Int, Notification> {
            val dataSource = NotificationDataSource(userId, token, viewModelScope)
            notificationLiveDataSource.postValue(dataSource)
            return dataSource
        }

        fun getNotificationLiveDataSource() = notificationLiveDataSource
    }
}

