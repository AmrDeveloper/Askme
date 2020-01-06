package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.models.Notification
import com.amrdeveloper.askme.net.NotificationService
import com.amrdeveloper.askme.net.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class NotificationViewModel @Inject constructor(private val notificationService: NotificationService): ViewModel() {

    private var notificationPagedList: LiveData<PagedList<Notification>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Notification>>

    fun loadUserNotifications(id : String, token : String){
        val dataSourceFactory = NotificationDataSourceFactory(id, token)
        liveDataSource = dataSourceFactory.getNotificationLiveDataSource()

        notificationPagedList = LivePagedListBuilder(dataSourceFactory, PagingConfig.getConfig()).build()
    }

    fun makeNotificationReaded(id : String, token : String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = notificationService.makeNotificationOpened(token, id)
                when(response.code()){
                    200 -> {
                        Log.d("NotificationViewModel", "Notification Request is done Now")
                    }
                    403 -> {
                        Log.d("NotificationViewModel", "No Auth")
                    }
                    else -> {

                    }
                }
            }catch (exception : Exception){
                Log.d("NotificationViewModel", "Notification Invalid Request")
            }
        }
    }

    fun getNotificationList() = notificationPagedList

    private inner class NotificationDataSourceFactory(var userId: String, val token: String) :
        DataSource.Factory<Int, Notification>() {

        private val notificationLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Notification>> =
            MutableLiveData()

        override fun create(): DataSource<Int, Notification> {
            val dataSource = NotificationDataSource(userId, token, viewModelScope, notificationService)
            notificationLiveDataSource.postValue(dataSource)
            return dataSource
        }

        fun getNotificationLiveDataSource() = notificationLiveDataSource
    }
}

